(ns syringe.dose
  (:require [clojure.pprint :refer [pprint]]))

(defn stringify [x]
  (let [s  (cond (string? x) x
                 (symbol? x) (str x)
                 (and (list? x) (= (first x) 'quote)) (str (second x))
                 :else (str x))]
    s))

;; namespace utilities
(defmacro publics
  "inspect public symbols of namespace"
  ([] `(publics *ns*))
  ([n] `(->> '~n
             stringify
             symbol
             ns-publics
             keys
             pprint)))

(defmacro refers
  "inspect referenced symbols of namespace"
  ([] `(refers *ns*))
  ([n] `(->> '~n
             stringify
             symbol
             ns-refers
             keys
             pprint)))

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
