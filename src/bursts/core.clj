(ns bursts.core
  (:require [clojure.java.io :as io]
            ;; [optimus.assets :as assets]
            ;; [optimus.optimizations :as optimizations]
            ;; [optimus.prime :as optimus]
            ;; [optimus.strategies :refer [serve-live-assets]]
            ;; [optimus-sass.core]
            [hiccup.page :refer [html5]]
            [stasis.core :as stasis]
            [clojure.string :as str]
            [clojure.edn :as edn]
            [clojure.pprint :as pp]
            [bursts.markdown :refer [md]]
            [bursts.components :refer [build-page]])
  (:use [markdown.core]
        [markdown.transformers]
        [clojure.algo.generic.functor :only (fmap)]))

(defn layout-page [page]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:name "viewport"
            :content "width=device-width, initial-scale=1.0"}]
    [:title "Jake's Blog"]
    [:link {:rel "stylesheet" :href "/tomorrow-night.css"}]
    [:link {:rel "stylesheet" :href "/typeplate.css"}]
    [:link {:rel "stylesheet" :href "/style.css"}]
    [:script {:src "/highlight.pack.js"}]
    [:script "hljs.initHighlightingOnLoad();"]]
   [:body
    [:div.logo
     [:h2.typl8-gamma "Transient Random-noise Bursts With Announcements"]]
    page]))

(def 💩 "💩")

(defn build-post [page]
  (let [[meta content] (str/split page #"-----")
        meta (edn/read-string meta)
        {:keys [title tags]} meta
        tags (map name tags)
        content (md content)]
    [:div.body
     [:h2.post-title.typl8-delta title]
     [:div.content content]
     [:p "Tags: " (str/join " " tags)]]))

(defn build-posts [posts]
  (map (comp (partial build-page "Jake's Blog") build-post) posts))

(defn post-pages [pages]
  (zipmap (map #(str/replace % #"\.md$" "/") (keys pages))
          (build-posts (vals pages))))

;; (defn partial-pages [pages]
;;   (zipmap (keys pages)
;;           (map layout-page (vals pages))))

(defn post-map [post]
  (let [[meta content] (str/split post #"-----")
        meta (edn/read-string meta)
        {:keys [title tags]} meta
        tags (map name tags)
        content (md content)]
    {:title title :tags tags :content content}))

(def posts-map
  (let [raw-posts (stasis/slurp-directory
                   "resources/md" #"\.md$")]
    (fmap post-map raw-posts)))

(defn get-pages []
  (stasis/merge-page-sources
   {:public
    (stasis/slurp-directory "resources/public" #".*\.(html|css|js)$")
    ;; :partials
    ;; (partial-pages (stasis/slurp-directory "resources/partials" #".*\.html$"))
    :posts
    (post-pages (stasis/slurp-directory "resources/md" #"\.md$"))}))

(def app (stasis/serve-pages get-pages))
