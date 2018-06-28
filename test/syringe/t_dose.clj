(ns syringe.t-dose
  (:require [syringe.dose :refer :all]
            [midje.sweet :refer :all]))

;; a bit brittle if they add new partition functions, but I want to test the default namespace too
(facts "ns-find"
  (ns-find partition clojure.core) => '(partition partition-all partition-by)
  (ns-find "partition") => '(partition partition-all partition-by)
  (ns-find 'partition 'clojure.core) => '(partition partition-all partition-by)
  (ns-find #"partition") => '(partition partition-all partition-by))
