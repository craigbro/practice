(ns practice.views.common
  (:use noir.core
        noir.request
        practice.models.posts
        hiccup.core
        hiccup.page-helpers))


(def site-title "Practice")

(def site-tagline "A place to share")

(def site-owner "Craig Brozefsky")

(def site-owner-email "craig@red-bean.com")


(defn server-name []
  (:server-name noir.request/*request*))

(defn server-port []
  (:server-port noir.request/*request*))

(defn server-scheme []
  (if (= (:scheme noir.request/*request*)
         :https)
    "https://"
    "http://"))

(defn server-url []
  (str (server-scheme) (server-name) ":" (server-port)))
  
(defpartial sidebar []
  [:section#sidebar
   [:h1 site-title ]
   [:p#tagline site-tagline]
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
            :title site-title
            :href (str (server-url) "/rss")}]
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

  