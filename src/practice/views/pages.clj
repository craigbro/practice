(ns practice.views.pages
  (:require [practice.views.common :as common])
  (:use noir.core
        practice.models.pages
        hiccup.core
        hiccup.page-helpers))

(defpage [:get "/about"] []
  (common/layout "About"
                 (page-body (lookup-page "about"))))
                  
