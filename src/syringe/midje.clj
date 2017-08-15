(ns syringe.midje
  (:require [midje.repl :as midje]))

(defn sat []
  (midje/autotest)
  (midje/autotest :stop))
