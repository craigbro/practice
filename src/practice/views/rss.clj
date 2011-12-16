(ns practice.views.rss
  (:use noir.core
        noir.request
        practice.models.posts
        practice.views.common
        hiccup.core)
  (:require [clojure.xml :as xml]))

(defn make-channel [title link description & optional]
  {:tag "channel"
   :attrs {}
   :content [{:tag "title" :content [title]}
             {:tag "link" :content [link]}
             {:tag "description" :content [description]}]})

(defn post-rss-item [p]
  {:tag "item"
   :content [{:tag "title" :content [(post-title p)]}
             {:tag "link" :content [(str (server-url) "/posts/" (:name p))]}
             {:tag "guid" :content [(str (server-url) "/posts/" (:name p))]}
             {:tag "pubDate" :content [(:date (:metadata p))]}
             {:tag "author" :content ["craig@red-bean.com (Craig Brozefsky)"]}
             {:tag "description" :content [(escape-html (post-body p))]}]})

(defn add-content [tag content]
  (assoc tag
    :content
    (concat (:content tag)
            content)))

(defn make-rss [channel]
  {:tag "rss"
   :attrs {"version" "2.0"}
   :content [channel]})

(defn feed-body []
  (with-out-str
    (clojure.xml/emit
     (make-rss
      (add-content
       (make-channel "Practice Blog"
                     (server-url)
                     "A place to share")
       (map
        post-rss-item
        posts))))))

(defpage [:get "/rss"] []
  {:headers {"Content-Type" "application/rss+xml"}
   :body (feed-body)})


        