(ns practice.views.posts
  (:require [practice.views.common :as common])
  (:use noir.core
        practice.models.posts
        hiccup.core
        hiccup.page-helpers))

(defpage [:get "/"] []
  (common/layout "Practice Blog"
                 (map
                  #(common/post-short %1)
                  posts)))

(defn show-post
  "Show the full version of the post"
  [p]
  (common/layout (post-title p)
                 (common/post-full p)))

(defpage [:get "/posts/"] []
  (let [p (first posts)]
    (show-post p)))

  
(defpage [:get "/posts/:name"] {:keys [name]}
  (if-let [p (lookup-post name)]
    (show-post p)
    (common/layout "Post Not Found"
                   [:h1 (str "Post " name "Not Found") ])))
