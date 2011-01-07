(ns clj-gravatar.test.core
  (:use [clj-gravatar.core] :reload)
  (:use [clojure.test]))

; http://ja.gravatar.com/site/implement/profiles/json/
(def *test-address* "beau@dentedreality.com.au")

(deftest test-map->get-parameter
  (are [x y] (= x y)
    nil (map->get-parameter {})
    "?a=1" (map->get-parameter {:a 1})
    "?a=1" (map->get-parameter {:a 1 :b nil})
    "?a=1&b=2" (map->get-parameter {:a 1 :b 2})
    "?a=1&b=2" (map->get-parameter {:a 1 :b 2 :c nil})
    "?s=http%3A%2F%2Fhoge.com%2F" (map->get-parameter {:s "http://hoge.com/"})
    )
  )

(deftest test-gravatar-image
  (are [x y] (= x y)
    "http://www.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50"
    (gravatar-image *test-address*)

    "https://secure.gravatar.com/avatar/205e460b479e2e5b48aec07710c08d50"
    (gravatar-image *test-address* :secure? true)
    )
  )

(deftest test-gravatar-profile
  (is (nil? (gravatar-profile "NOTFOUNDUSER")))
  (let [profile (gravatar-profile *test-address*)]
    (is profile)
    (are [x y] (= x y)
      "1428" (-> profile :entry first :id)
      "205e460b479e2e5b48aec07710c08d50" (-> profile :entry first :hash)
      *test-address* (-> profile :entry first :emails first :value)
      )
    )
  )
