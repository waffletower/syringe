(ns syringe.middleware
  (:require [clojure.tools.nrepl.middleware :refer [set-descriptor!]]
            [clojure.java.io :as io]
            [environ.core :refer [env]]))

;; need to have a unique file handle per repl, perhaps a timestamped file name
(def output-file "/tmp/works.edn")
(def osa (atom (io/writer output-file :append true)))

(defn repl-journaling [handler]
  (fn [{:keys [op] :as request}]
    (when (= op "eval")
      (.write @osa (-> request
                       (select-keys [:op :code :ns :file])
                       (assoc :timestamp (str (java.time.LocalDateTime/now)))
                       (prn-str)))
      (.flush @osa))
    (handler request)))

(set-descriptor! #'repl-journaling
  {:requires #{}
   :expects #{}
   :handles {"eval" {:doc "journals repl code evaluation strings"}}})
