(ns syringe.nrepl
  (:require [clojure.java.shell :refer [sh]]
            [clojure.string :as string]))

(defn nsa
  "`nsa` - naive shell args.
  primitive way to split whitespace delimited shell arguments into a collection
  for consumption with `clojure.java.shell.sh`"
  [s]
  (string/split s #"\s+"))

(defn local-nrepl-processes
  []
  (->> (sh "ps" "u")
       :out
       #_(keep (partial re-matches #"leiningen\.original\.pwd=(.+)\s+" ))))

;; someday we might nerd out and create an `sa` function with a true shell argument parser

;; (defvar cider-ps-running-nrepls-command "ps u | grep leiningen"
;;   "Process snapshot command used in `cider-locate-running-nrepl-ports'.")

;; (defvar cider-ps-running-nrepl-path-regexp-list
;;   '("\\(?:leiningen.original.pwd=\\)\\(.+?\\) -D"
;;     "\\(?:-classpath +:?\\(.+?\\)/self-installs\\)")
;;   "Regexp list to get project paths.
;;  Extract project paths from output of `cider-ps-running-nrepls-command'.
;;  Sub-match 1 must be the project path.")
