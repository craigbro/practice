;; RSS Feed Support
;; We are working from the RSS spec at:
;; http://cyber.law.harvard.edu/rss/index.html
;;
;; This is a simple RSS feed generator that puts the blog posts in
;; reverse chrono order.  It's a full text feed, with the whole post
;; in the description field.  I prefer this as a user, as it means I
;; can DL the feed and read offline, like on my phone with spotty
;; reception.

(ns practice.views.rss
  (:use noir.core
        practice.models.posts
        practice.views.common)
  (:require [clojure.xml :as xml]))

(def max-posts
  "Max posts to put in the rss feed."
  42)

(defn date-as-rfc822 [#^java.util.Date date]
  (.format
   (java.text.SimpleDateFormat.
    "EEE', 'dd' 'MMM' 'yyyy' 'HH:mm:ss' 'Z",)
   date))

(defn owner []
  (str site-owner-email " (" site-owner ")"))

(defn blog-channel [items]
  {:tag "channel"
   :content (concat
             [{:tag "title" :content [site-title]}
              {:tag "link" :content [(server-url)]}
              {:tag "language" :content ["en-us"]}
              {:tag "webMaster" :content [(owner)]}
              {:tag "managingEditor" :content []}
              {:tag "copyright" :content ["Public Domain"]}
              {:tag "description" :content [site-tagline]}]
             items)})

(defn post-rss-item [p]
  "Given a post, return a XML item tag (as a map) representing the post.  We put the full text in the description"
  {:tag "item"
   :content [{:tag "title" :content [(post-title p)]}
             {:tag "link" :content [(str (server-url) "/posts/" (:name p))]}
             {:tag "guid" :content [(str (server-url) "/posts/" (:name p))]}
             {:tag "pubDate" :content [(date-as-rfc822
                                        (java.util.Date. (:date (:metadata p))))]}
             {:tag "author" :content ["craig@red-bean.com (Craig Brozefsky)"]}
             {:tag "description" :content [(escape-html (post-body p))]}]})

(defn add-content [tag content]
  (assoc tag
    :content
    (concat (:content tag)
            content)))

(defn rss-feed [channel]
  {:tag "rss"
   :attrs {"version" "2.0"}
   :content [channel]})

(defn feed-body []
  (with-out-str
    (clojure.xml/emit
     (rss-feed
      (blog-channel
       (map
        post-rss-item
        (take max-posts posts)))))))

(defpage [:get "/rss"] []
  {:headers {"Content-Type" "application/rss+xml"}
   :body (feed-body)})
