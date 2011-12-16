(ns practice.views.rss
  (:use noir.core
        practice.models.posts
        hiccup.core)
  (:require [clojure.xml :as xml]))

(defn make-channel [title link description & optional]
  {:tag "channel"
   :attrs {}
   :content [{:tag "title" :content [title]}
             {:tag "link" :content [link]}
             {:tag "description" :content [description]}]})

(defn make-item [title link description]
  {:tag "item"
   :content [{:tag "title" :content [title]}
             {:tag "link" :content [link]}
             {:tag "description" :content [description]}]})


(defn add-content [tag content]
  (assoc tag
    :content
    (concat (:content tag)
            content)))

(defn make-rss [channel]
  {:tag "rss"
   :content [channel]})


(defn feed-body []
  (with-out-str
    (clojure.xml/emit
     (make-rss
      (add-content
       (make-channel "Practice Bloc"
                     "http://practice.kokonino.net"
                     "A place to share")
       (map
        (fn [p]
          (make-item (post-title p)
                     (str "http://practice.kokonino.net/posts/" (:name p))
                     (escape-html (post-body p))))
        posts))))))


(defpage [:get "/rss"] []
  {:content-type "application/rss+xml"
   :status 200
   :body (feed-body)})


        