(ns syringe.dose
  (:require [clojure.pprint :refer [pprint]]
            [puget.printer :as puget]))

;; don't privatize functions invoked by macros
(defn handle-aliases [sym]
  (let [aliases (ns-aliases *ns*)]
    (if-let [namespace (get aliases sym)]
      (ns-name namespace)
      sym)))

(defn stringify [x]
  (let [s  (cond (string? x) x
                 (symbol? x) (str x)
                 (and (list? x) (= (first x) 'quote)) (str (second x))
                 :else (str x))]
    s))

;; namespace utilities
(defmacro publics
  "inspect public symbols of namespace"
  ([] `(publics ~*ns*))
  ([n] `(->> '~n
             stringify
             symbol
             handle-aliases
             ns-publics
             keys)))

(defmacro refers
  "inspect referenced symbols of namespace"
  ([] `(refers ~*ns*))
  ([n] `(->> '~n
             stringify
             symbol
             handle-aliases
             ns-refers
             keys)))

(defmulti coerce-pattern class)
(defmethod coerce-pattern java.util.regex.Pattern [x] x)
(defmethod coerce-pattern java.lang.String [x] (re-pattern x))

(defn seq-matches
  "filter a sequence by coercing items to a string and applying re-find with a supplied regex
  re - regex pattern (or string which will be coerced to a pattern)
  sq - sequence of strings or string coercible objects"
  [re sq]
  (filter (fn [x] (re-find (coerce-pattern re) (str x))) sq))

(defmacro ns-find
  "perform a regex search of public symbols in a namespace
  re - regex pattern (or string which will be coerced to a pattern)
  n - namespace symbol (either quoted or unquoted via stringify)"
  ([re] `(ns-find ~re 'clojure.core))
  ([re n] `(seq-matches ~re (publics ~n))))

(defn resolve-fqns [s] (->> (resolve s) meta :ns))

(defmacro fqns
  "poll fully qualified namespace of symbol 's'"
  ([] `(resolve-fqns *ns*))
  ([symbol-or-string] `(resolve-fqns (symbol (stringify '~symbol-or-string)))))

(defn get-classpath []
  (sort (map (memfn getPath)
             (seq (.getURLs (java.lang.ClassLoader/getSystemClassLoader))))))

(defn list-all-ns []
  (pprint (map ns-name (all-ns))))

;; golf conbini
(defn p [& args]
  (apply pprint args))

(defn cp [& args]
  (apply puget/cprint args))
