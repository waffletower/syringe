(ns syringe.dose
  (:require [clojure.pprint :refer [pprint]]
            [puget.printer :as puget]))

;; don't privatize functions invoked by macros
(defn handle-aliases [sym]
  (let [aliases (ns-aliases *ns*)]
    (if-let [namespace (get aliases sym)]
      (ns-name namespace)
      sym)))

(defn re-pattern? [x]
  (instance? java.util.regex.Pattern x))

(defn stringify [x]
  (let [s (cond (string? x) x
                (symbol? x) (str x)
                (and (list? x) (= (first x) 'quote)) (str (second x))
                :else (str x))]
    s))

(defn patternize [x]
  (let [p (cond (string? x) (re-pattern x)
                (symbol? x) (re-pattern (name x))
                (coll? x) (re-pattern (-> x second name))
                (re-pattern? x) x
                :else (re-pattern (str x)))]
    p))

;; namespace utilities
(defmacro publics
  "inspect public symbols of namespace"
  ([] `(publics ~*ns*))
  ([n] `(->> '~n
             stringify
             symbol
             handle-aliases
             ns-publics
             keys
             sort)))

(defmacro interns
  "inspect public symbols of namespace"
  ([] `(publics ~*ns*))
  ([n] `(->> '~n
             stringify
             symbol
             handle-aliases
             ns-interns
             keys
             sort)))

(defmacro refers
  "inspect referenced symbols of namespace"
  ([] `(refers ~*ns*))
  ([n] `(->> '~n
             stringify
             symbol
             handle-aliases
             ns-refers
             keys
             sort)))

(defmacro seq-matches
  "filter a sequence by coercing items to a string and applying re-find with a supplied regex
  re - regex pattern (or string which will be coerced to a pattern)
  sq - sequence of strings or string coercible objects"
 [re sq]
  (let [p# (patternize re)]
    `(filter (fn [x#] (re-find ~p# (str x#))) ~sq)))

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

(defn list-all-ns []
  (pprint (map ns-name (all-ns))))

;; golf conbini
(defn p [& args]
  (apply pprint args))

(defn cp [& args]
  (apply puget/cprint args))
