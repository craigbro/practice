(ns practice.models.pages
  (:use noir.core))

(def page-dir "resources/pages/")

(defn load-all-page-metadata []
  (let [p (re-pattern "\\.")]
    (map
     (fn [fname]
       {:tag ::page
        :name (first (clojure.string/split fname p))
        :filename fname
        })
     (filter
      #(not (re-matches (re-pattern ".*~") %))
      (.list (java.io.File. (str page-dir ".")))))))

(def pages (load-all-page-metadata))


(defn lookup-page [name]
  (first (filter #(= (:name %1) name) pages)))

(defn page-body [page]
  (.markdown (com.petebevin.markdown.MarkdownProcessor.)
             (slurp (str page-dir (:filename page)))))

(defn page-title [page]
  (:name page))