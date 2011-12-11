(ns practice.models.posts
  (:use noir.core))

(def post-dir "resources/posts/")

(defn load-all-post-metadata []
  (let [p (re-pattern "\\.")]
    (map
     (fn [fname]
       {:tag ::post
        :name (first (clojure.string/split fname p))
        :filename fname
        :metadata (read-string (-> (str post-dir fname)
                                   java.io.FileReader.
                                   java.io.BufferedReader.
                                   .readLine))
        })
     (filter
      #(not (re-matches (re-pattern ".*~") %))
      (.list (java.io.File. (str post-dir ".")))))))

(def posts (load-all-post-metadata))

(defn lookup-post [name]
  (first (filter #(= (:name %1) name) posts)))

(defn post-body [post]
  (let [[data text] (clojure.string/split (slurp (str post-dir (:filename post))) (re-pattern "\n|\r\n") 2)]
    (.markdown (com.petebevin.markdown.MarkdownProcessor.) text)))

(defn post-title [post]
  (or (:title post) (:name post)))