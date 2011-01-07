(ns clj-gravatar.core
  (:require
     [clojure.contrib.string :as string]
     [clojure.contrib.io :as io]
     [clojure.contrib.json :as json]
     )
  (:import
     [java.net URLEncoder]
     [java.security MessageDigest]
     )
  )

; util {{{
(defn bytes->hex-str [bytes]
  (apply str (map #(string/tail 2 (str "0" (Integer/toHexString (bit-and 0xff %)))) bytes))
  )
(defn digest-hex [algorithm s]
  (if-not (string/blank? s)
    (-> (MessageDigest/getInstance algorithm) (.digest (.getBytes s)) bytes->hex-str)
    )
  )
(def str->md5 (partial digest-hex "MD5"))
(defn- encode [x] (if (string? x) (URLEncoder/encode x) x))
; }}}

(defn- gravatar-url [secure?]
  (if secure?
    "https://secure.gravatar.com/"
    "http://www.gravatar.com/"
    )
  )

; =map->get-parameter
(defn map->get-parameter
  "convert map to GET parameter"
  [m]
  (let [ls (map (fn [[k v]] (str (name k) "=" (encode v)))
                (remove #(-> % second nil?) m))]
    (if-not (empty? ls)
      (str "?" (string/join "&" ls))
      )
    )
  )

; =gravatar-image
(defn gravatar-image
  "get gravatar image url from mail address"
  [mail-address & {:keys [size default secure?] :or {size nil, default nil, secure? false}}]
  (if-not (string/blank? mail-address)
    (str (gravatar-url secure?)
         "avatar/"
         (str->md5 mail-address)
         (map->get-parameter {:s size :d default})
         )
    )
  )

; =gravatar-profile
(defn gravatar-profile
  "get gravatar profile from mail address"
  [mail-address & {:keys [secure?] :or {secure? false}}]
  (if-not (string/blank? mail-address)
    (let [url (str (gravatar-url secure?) (str->md5 mail-address) ".json")]
      (try
        (json/read-json (apply str (io/read-lines url)))
        (catch Exception e nil)
        )
      )
    )
  )
