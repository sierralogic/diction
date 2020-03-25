(ns diction.parser
  (:require [markdown.core :as md]
            [markdown.transformers :as mt]
            [markdown.links
             :refer [link
                     image
                     reference-link
                     image-reference-link
                     implicit-reference-link
                     footnote-link]]
            [markdown.lists :refer [li]]
            [markdown.tables :refer [table]]
            [markdown.common
             :refer
             [escape-code
              escaped-chars
              freeze-string
              separator
              thaw-strings
              strong
              bold
              bold-italic
              em
              italics
              strikethrough
              inline-code
              escape-inhibit-separator
              inhibit
              make-heading
              dashes]]
            ))


(def transformer-vector-suppress-italics
  [mt/set-line-state
   mt/empty-line
   inhibit
   escape-inhibit-separator
   mt/code
   mt/codeblock
   escaped-chars
   inline-code
   mt/autoemail-transformer
   mt/autourl-transformer
   image
   image-reference-link
   link
   implicit-reference-link
   reference-link
   footnote-link
   mt/hr
   mt/blockquote-1
   li
   mt/heading
   mt/blockquote-2
   ;; italics
   bold-italic
   em
   strong
   bold
   strikethrough
   mt/superscript
   table
   mt/paragraph
   mt/br
   thaw-strings
   dashes
   mt/clear-line-state])
