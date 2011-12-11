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
                  (reverse
                   (sort-by :date posts)))))
                  

(defpage [:get "/posts/"] []
  (let [p (first posts)]
    (common/layout (post-title p)
                   (common/post-full p))))

(defpage [:get "/posts/:name"] {:keys [name]}
  (if-let [p (lookup-post name)]
    (common/layout (post-title p)
                   (common/post-full p))
    (common/layout "Post Not Found"
                   [:h1 (str "Post " name "Not Found") ])))


                