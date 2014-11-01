package com.jparkie.givesmehope.utils;

import com.jparkie.givesmehope.models.Anthology;
import com.jparkie.givesmehope.models.Story;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class GivesMeHopeParser {
    public static Anthology mapHtmlToTrendingAnthology(String unparsedHtml) throws IllegalArgumentException {
        // This variables "CONSTANTS" are not scoped to the class but the function because GivesMeHope has no official API and this GET request was constructed by reading the HTML.
        final String CONSTANT_MAIN_DATA_ELEMENT = "page_wrapper";
        final String CONSTANT_STORY_ELEMENTS = "div.post[id^=post]";
        final String CONSTANT_STORY_BODY_ELEMENT = "div.body";
        final String CONSTANT_STORY_FOOTER_ELEMENT = "div.footer";
        final String CONSTANT_NEXT_PAGE_URL_ELEMENT = "next_link";

        Anthology trendingAnthology = null;
        List<Story> trendingAnthologyStories = null;
        String trendingAnthologyNextPageUrl = null;

        try {
            Document parsedDocument = Jsoup.parse(unparsedHtml);

            Element mainDataElement = parsedDocument.getElementById(CONSTANT_MAIN_DATA_ELEMENT);

            Elements storyElements = mainDataElement.select(CONSTANT_STORY_ELEMENTS);
            if (storyElements != null) {
                Element currentStoryElement = null;
                Element currentStoryBodyElement = null;
                Element currentStoryFooterElement = null;

                trendingAnthologyStories = new ArrayList<Story>();

                for (int index = 0; index < storyElements.size(); index++) {
                    try {
                        currentStoryElement = storyElements.get(index);
                        currentStoryBodyElement = currentStoryElement.select(CONSTANT_STORY_BODY_ELEMENT).first();
                        currentStoryFooterElement = currentStoryElement.select(CONSTANT_STORY_FOOTER_ELEMENT).first();

                        Story currentStory = null;
                        String currentStoryUrl = null;
                        String currentPostId = null;
                        String currentStoryFooter = null;
                        String currentStoryTitle = null;
                        String currentStoryCategory = null;
                        String currentStoryStory = null;

                        // Volatile Meta Data: No Constants.
                        currentStoryUrl = currentStoryFooterElement.select("div.fb-like").first().attr("data-href");
                        currentPostId = currentStoryUrl.substring(currentStoryUrl.lastIndexOf("/") + 1).replace("?m", "");

                        currentStoryFooter = currentStoryFooterElement.text().replace("Tweet", "").trim();

                        if (currentStoryBodyElement.select("strong").first() != null) {
                            currentStoryTitle = currentStoryBodyElement.select("strong").first().text();
                        }

                        if (currentStoryUrl.contains("/Amazing+Friends/")) {
                            currentStoryCategory = Story.CATEGORY_AMAZING_FRIENDS;
                        } else if (currentStoryUrl.contains("/Cute+Kids/")) {
                            currentStoryCategory = Story.CATEGORY_CUTE_KIDS;
                        } else if (currentStoryUrl.contains("/Inspiring+Feats/")) {
                            currentStoryCategory = Story.CATEGORY_INSPIRING_FEATS;
                        } else if (currentStoryUrl.contains("/Other/")) {
                            currentStoryCategory = Story.CATEGORY_OTHER;
                        } else if (currentStoryUrl.contains("/Potter/")) {
                            currentStoryCategory = Story.CATEGORY_POTTER;
                        } else if (currentStoryUrl.contains("/Random+Acts+Of+Kindness/")) {
                            currentStoryCategory = Story.CATEGORY_RANDOM_ACTS_OF_KINDNESS;
                        }

                        if (currentStoryTitle != null) {
                            currentStoryStory = currentStoryBodyElement.text().replace(currentStoryTitle, "").trim();
                        } else {
                            currentStoryStory = currentStoryBodyElement.text().trim();
                        }

                        currentStory = new Story();
                        currentStory.setUrl(currentStoryUrl);
                        currentStory.setPostId(currentPostId);
                        currentStory.setFooter(currentStoryFooter);
                        currentStory.setTitle(currentStoryTitle);
                        currentStory.setCategory(currentStoryCategory);
                        currentStory.setStory(currentStoryStory);

                        trendingAnthologyStories.add(currentStory);
                    } catch (NullPointerException e) {
                        continue;
                    }
                }
            } else {
                trendingAnthologyStories = new ArrayList<Story>();
            }

            Element nextPageUrlElement = parsedDocument.getElementById(CONSTANT_NEXT_PAGE_URL_ELEMENT);
            if (nextPageUrlElement != null) {
                trendingAnthologyNextPageUrl = Constants.BASE_URL + nextPageUrlElement.attr("href").replace("//", "/");
            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Invalid HTML");
        }

        trendingAnthology = new Anthology();
        trendingAnthology.setStories(trendingAnthologyStories);
        trendingAnthology.setNextPageUrl(trendingAnthologyNextPageUrl);

        return trendingAnthology;
    }

    public static Anthology mapHtmlToHotAnthology(String unparsedHtml) throws IllegalArgumentException {
        // This variables "CONSTANTS" are not scoped to the class but the function because GivesMeHope has no official API and this GET request was constructed by reading the HTML.
        final String CONSTANT_MAIN_DATA_ELEMENT = "page_wrapper";
        final String CONSTANT_STORY_ELEMENTS = "div.post[id^=post]";
        final String CONSTANT_STORY_BODY_ELEMENT = "div.body";
        final String CONSTANT_STORY_FOOTER_ELEMENT = "div.footer";
        final String CONSTANT_NEXT_PAGE_URL_ELEMENT = "next_link";

        Anthology hotAnthology = null;
        List<Story> hotAnthologyStories = null;
        String hotAnthologyNextPageUrl = null;

        try {
            Document parsedDocument = Jsoup.parse(unparsedHtml);

            Element mainDataElement = parsedDocument.getElementById(CONSTANT_MAIN_DATA_ELEMENT);

            Elements storyElements = mainDataElement.select(CONSTANT_STORY_ELEMENTS);
            if (storyElements != null) {
                Element currentStoryElement = null;
                Element currentStoryBodyElement = null;
                Element currentStoryFooterElement = null;

                hotAnthologyStories = new ArrayList<Story>();

                for (int index = 0; index < storyElements.size(); index++) {
                    try {
                        currentStoryElement = storyElements.get(index);
                        currentStoryBodyElement = currentStoryElement.select(CONSTANT_STORY_BODY_ELEMENT).first();
                        currentStoryFooterElement = currentStoryElement.select(CONSTANT_STORY_FOOTER_ELEMENT).first();

                        Story currentStory = null;
                        String currentStoryUrl = null;
                        String currentPostId = null;
                        String currentStoryFooter = null;
                        String currentStoryCategory = null;
                        String currentStoryImageUrl = null;

                        // Volatile Meta Data: No Constants.
                        currentStoryUrl = currentStoryBodyElement.select("a").first().attr("href");
                        currentPostId = currentStoryUrl.substring(currentStoryUrl.lastIndexOf("/") + 1).replace("?m", "");

                        currentStoryFooter = currentStoryFooterElement.text().replace("Tweet", "").trim();

                        if (currentStoryUrl.contains("/Amazing+Friends/")) {
                            currentStoryCategory = Story.CATEGORY_AMAZING_FRIENDS;
                        } else if (currentStoryUrl.contains("/Cute+Kids/")) {
                            currentStoryCategory = Story.CATEGORY_CUTE_KIDS;
                        } else if (currentStoryUrl.contains("/Inspiring+Feats/")) {
                            currentStoryCategory = Story.CATEGORY_INSPIRING_FEATS;
                        } else if (currentStoryUrl.contains("/Other/")) {
                            currentStoryCategory = Story.CATEGORY_OTHER;
                        } else if (currentStoryUrl.contains("/Potter/")) {
                            currentStoryCategory = Story.CATEGORY_POTTER;
                        } else if (currentStoryUrl.contains("/Random+Acts+Of+Kindness/")) {
                            currentStoryCategory = Story.CATEGORY_RANDOM_ACTS_OF_KINDNESS;
                        }

                        currentStoryImageUrl = currentStoryBodyElement.select("img").first().attr("src");

                        currentStory = new Story();
                        currentStory.setUrl(currentStoryUrl);
                        currentStory.setPostId(currentPostId);
                        currentStory.setFooter(currentStoryFooter);
                        currentStory.setCategory(currentStoryCategory);
                        currentStory.setImageUrl(currentStoryImageUrl);

                        hotAnthologyStories.add(currentStory);
                    } catch (NullPointerException e) {
                        continue;
                    }
                }
            } else {
                hotAnthologyStories = new ArrayList<Story>();
            }

            Element nextPageUrlElement = parsedDocument.getElementById(CONSTANT_NEXT_PAGE_URL_ELEMENT);
            if (nextPageUrlElement != null) {
                hotAnthologyNextPageUrl = Constants.BASE_URL + nextPageUrlElement.attr("href").replace("//", "/");
            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Invalid HTML");
        }

        hotAnthology = new Anthology();
        hotAnthology.setStories(hotAnthologyStories);
        hotAnthology.setNextPageUrl(hotAnthologyNextPageUrl);

        return hotAnthology;
    }

    public static Story mapHtmlToVoteStory(String unparsedHtml) throws IllegalArgumentException {
        // This variables "CONSTANTS" are not scoped to the class but the function because GivesMeHope has no official API and this GET request was constructed by reading the HTML.
        final String CONSTANT_STORY_ELEMENTS = "div.post[id^=post]";
        final String CONSTANT_STORY_BODY_ELEMENT = "div.body";
        final String CONSTANT_STORY_FOOTER_ELEMENT = "div.footer";

        Story voteStory = new Story();

        try {
            Document parsedDocument = Jsoup.parse(unparsedHtml);

            Element storyElement = parsedDocument.select(CONSTANT_STORY_ELEMENTS).first();
            if (storyElement != null) {
                Element storyBodyElement = null;
                Element storyFooterElement = null;

                storyBodyElement = storyElement.select(CONSTANT_STORY_BODY_ELEMENT).first();
                storyFooterElement = storyElement.select(CONSTANT_STORY_FOOTER_ELEMENT).first();

                String storyUrl = null;
                String storyPostId = null;
                String storyFooter = null;
                String storyCategory = null;
                String storyImageUrl = null;

                // Volatile Meta Data: No Constants.
                storyUrl = storyBodyElement.select("a").first().attr("href");
                storyPostId = storyUrl.substring(storyUrl.lastIndexOf("/") + 1).replace("?m", "");

                storyFooter = storyFooterElement.text().replace("Tweet", "").trim();

                if (storyUrl.contains("/Amazing+Friends/")) {
                    storyCategory = Story.CATEGORY_AMAZING_FRIENDS;
                } else if (storyUrl.contains("/Cute+Kids/")) {
                    storyCategory = Story.CATEGORY_CUTE_KIDS;
                } else if (storyUrl.contains("/Inspiring+Feats/")) {
                    storyCategory = Story.CATEGORY_INSPIRING_FEATS;
                } else if (storyUrl.contains("/Other/")) {
                    storyCategory = Story.CATEGORY_OTHER;
                } else if (storyUrl.contains("/Potter/")) {
                    storyCategory = Story.CATEGORY_POTTER;
                } else if (storyUrl.contains("/Random+Acts+Of+Kindness/")) {
                    storyCategory = Story.CATEGORY_RANDOM_ACTS_OF_KINDNESS;
                }

                storyImageUrl = storyBodyElement.select("img").first().attr("src");

                voteStory.setUrl(storyUrl);
                voteStory.setPostId(storyPostId);
                voteStory.setFooter(storyFooter);
                voteStory.setCategory(storyCategory);
                voteStory.setImageUrl(storyImageUrl);
            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Invalid HTML");
        }

        return voteStory;
    }
}
