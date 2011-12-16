(ns practice.views.common
  (:use noir.core
        practice.models.posts
        hiccup.core
        hiccup.page-helpers))

(defpartial sidebar []
  [:section#sidebar
   [:h1 "Practice"]
   [:p#tagline
    "A place to share"]
   [:nav
    [:ul
     [:li [:a {:href "/"} "Home" ]]
     [:li [:a {:href "/about"} "About" ]]
     [:li [:a {:href "/feedback"} "Feedback" ]]
     ]
    ]
   ])

(defpartial footer []
  [:div#footer
   [:p "Premature reification is the root of all evil."]])
     

(defpartial layout [title & content]
  (html5
   [:head
    (include-css "/css/zh_style.css")
    [:title title]
    [:link {:rel "alternate"
            :type "application/rss+xml"
            :title "Practice Blog"
            :href "http://practice.kokonino.net/rss"}]
    ]
   [:body
    [:div#container
     [:div#content
      [:div.entry
       content]
      (sidebar)]
     (footer)]]))
     
(defpartial post-short [post]
  [:div.post-short
   [:h2.post-title
    [:a {:href (str "/posts/" (:name post))} (post-title post)]]
   [:div.post
    (post-body post)
    [:div.post-footer
     [:p (str "Posted: " (:date (:metadata post)))
      [:a.feedback-link {:href "/feedback"} "Feedback"]]]]])

(defpartial post-full [post]
  [:div.post-full
   [:h2.post-title (post-title post)]
   [:div.post
    (post-body post)
    [:div.post-footer
     [:p (str "Posted: " (:date (:metadata post)))]]]])

  