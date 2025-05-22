(ns syringe.dose
  (:require [clojure.pprint :refer [pprint *print-right-margin*]]
            [clojure.reflect :as reflect]
            [clojure.java.classpath :as cp]
            [clojure.tools.namespace.find :as find]
            [puget.printer :as puget]))

;; don't privatize functions invoked by macros
;; prefer meadows to razor-wire and chain-link fencing
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
  "inspect public symbols of namespace.
  `n` namespace -- can be an namespace symbol alias."
  ([] `(publics ~*ns*))
  ([n] `(->> '~n
             stringify
             symbol
             handle-aliases
             ns-publics
             keys
             sort)))

(defmacro interns
  "inspect public symbols of namespace.
  `n` namespace -- can be an namespace symbol alias."
  ([] `(publics ~*ns*))
  ([n] `(->> '~n
             stringify
             symbol
             handle-aliases
             ns-interns
             keys
             sort)))

(defmacro refers
  "inspect referenced symbols of namespace.
  `n` namespace -- can be an namespace symbol alias."
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
  re - regex pattern (symbols or strings will be coerced to a pattern)
  sq - sequence of strings or string coercible objects"
 [re sq]
  (let [p# (patternize re)]
    `(filter (fn [x#] (re-find ~p# (str x#))) ~sq)))

(defmacro ns-find
  "perform a regex search of public symbols in a namespace
  re - regex pattern (symbols or strings will be coerced to a pattern)
  n - namespace symbol (either quoted or unquoted via stringify)"
  ([re] `(ns-find ~re 'clojure.core))
  ([re n] `(seq-matches ~re (publics ~n))))

(defn resolve-fqns [s] (->> (resolve s) meta :ns))

(defmacro fqns
  "poll fully qualified namespace of symbol 's'"
  ([] `(resolve-fqns *ns*))
  ([symbol-or-string] `(resolve-fqns (symbol (stringify '~symbol-or-string)))))

(defn ponder
  "Show public methods of `x` without packaging"
  [x]
  (->> (reflect/reflect x)
       :members
       (filter (comp #(contains? % :public) :flags))
       (sort-by :name)
       vec))

(defn list-ns
  "outputs sorted collection of namespace symbols via `clojure.java.classpath/classpath` and
  `clojure.tools.namespace.find/find-namespaces`."
  []
  (->> (cp/classpath)
       find/find-namespaces
       sort))

(defmacro filter-ns
  "outputs sorted collection of namespace symbols via `clojure.java.classpath/classpath` and
  `clojure.tools.namespace.find/find-namespaces`.
  Allows regex symbol filtering via optional `re` argument (regex, string, or even symbols)."
  ([] `(list-ns))
  ([re]
   (let [p# (when re (patternize re))]
     `(let [ss# (list-ns)]
        (if ~p#
          (filter (fn [x#] (re-find ~p# (str x#))) ss#)
          ss#)))))

;; golf conbini
(defn p [& args]
  (apply pprint args))

(defn cp [& args]
  (apply puget/cprint args))

(defn wp [x]
  (binding [*print-right-margin* 100]
    (pprint x)))

(defn wwp [x]
  (binding [*print-right-margin* 200]
    (pprint x)))
