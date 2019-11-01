/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.post;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *         Post post = PostBuilder.newPost();
 *
 *         Language zhCn = post.createZhCnLanguage("I'm title");
 *
 *         zhCn.createLine()
 *                 .createTextTag("line-1 text", false)
 *                 .creatATag("link to google", "https://www.google.com");
 *
 *         zhCn.createLine()
 *                 .createTextTag("line-2 text", false)
 *                 .creatATag("link to github", "https://github.com");
 */
public class PostBuilder {

    public static Post newPost() {
        return new Post();
    }

    public static Language newLanguage() {
        return new Language("");
    }

    public static class Post {
        private Map<String, Language> languages = new HashMap<>(3);

        public Language createZhCnLanguage(String title) {
            return createLanguage("zh_cn", title);
        }

        public Language createJaJpLanguage(String title) {
            return createLanguage("ja_jp", title);
        }

        public Language createEnUsLanguage(String title) {
            return createLanguage("en_us", title);
        }

        public Object toContent() {
            return languages.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toLanguageObject()));
        }

        private Language createLanguage(String code, String title) {
            Language l = new Language(title);
            languages.put(code, l);
            return l;
        }
    }


    public static class Language {

        private String title;

        private List<Line> lines = new ArrayList<>(4);

        Language(String title) {
            this.title = title;
        }

        public Line createLine() {
            Line n = new Line();
            lines.add(n);
            return n;
        }

        public Object toLanguageObject() {
            Map<String, Object> o = new HashMap<>(2);
            o.put("title", title);
            o.put("content", lines.stream().map(Line::toLineObject).collect(Collectors.toList()));
            return o;
        }
    }

    public static class Line {
        private List<Tag> tags = new ArrayList<>(4);

        public Line createTextTag(String text, boolean unEscape) {
            Tag t = createTag("text").addElement("text", text);
            if (unEscape) {
                t.addElement("un_escape", true);
            }
            return this;
        }

        public Line creatATag(String text, String href) {
            createTag("a").addElement("text", text).addElement("href", href);
            return this;
        }

        public Line creatAtTag(String userId) {
            createTag("at").addElement("user_id", userId);
            return this;
        }

        public Line createImgTag(String imageKey, int width, int height) {
            createTag("img")
                    .addElement("image_key", imageKey)
                    .addElement("width", width)
                    .addElement("height", height);
            return this;
        }

        private Tag createTag(String tagType) {
            Tag tag = new Tag(tagType);
            tags.add(tag);
            return tag;
        }

        public Object toLineObject() {
            return tags.stream().map(Tag::toTagObject).collect(Collectors.toList());
        }
    }

    static class Tag {
        private String tag;

        private Map<String, Object> elements = new HashMap<>(4);

        Tag(String tag) {
            this.tag = tag;
        }

        Tag addElement(String k, Object o) {
            elements.put(k, o);
            return this;
        }

        public Object toTagObject() {
            Map<String, Object> o = new HashMap<>(elements.size() + 1);
            o.putAll(elements);
            o.put("tag", tag);
            return o;
        }
    }
}
