package android.trithe.sqlapp.model;

import java.util.ArrayList;
import java.util.List;

public class Json {


    private int id;
    private String date;
    private String date_gmt;
    private GuidBean guid;
    private String modified;
    private String modified_gmt;
    private String slug;
    private String status;
    private String type;
    private String link;
    private TitleBean title;
    private int author;
    private String comment_status;
    private String ping_status;
    private String template;
    private DescriptionBean description;
    private CaptionBean caption;
    private String alt_text;
    private String media_type;
    private String mime_type;
    private MediaDetailsBean media_details;
    private int post;
    private String source_url;
    private LinksBean _links;
    private List<?> meta;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate_gmt() {
        return date_gmt;
    }

    public void setDate_gmt(String date_gmt) {
        this.date_gmt = date_gmt;
    }

    public GuidBean getGuid() {
        return guid;
    }

    public void setGuid(GuidBean guid) {
        this.guid = guid;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getModified_gmt() {
        return modified_gmt;
    }

    public void setModified_gmt(String modified_gmt) {
        this.modified_gmt = modified_gmt;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public TitleBean getTitle() {
        return title;
    }

    public void setTitle(TitleBean title) {
        this.title = title;
    }

    public int getAuthor() {
        return author;
    }

    public void setAuthor(int author) {
        this.author = author;
    }

    public String getComment_status() {
        return comment_status;
    }

    public void setComment_status(String comment_status) {
        this.comment_status = comment_status;
    }

    public String getPing_status() {
        return ping_status;
    }

    public void setPing_status(String ping_status) {
        this.ping_status = ping_status;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public DescriptionBean getDescription() {
        return description;
    }

    public void setDescription(DescriptionBean description) {
        this.description = description;
    }

    public CaptionBean getCaption() {
        return caption;
    }

    public void setCaption(CaptionBean caption) {
        this.caption = caption;
    }

    public String getAlt_text() {
        return alt_text;
    }

    public void setAlt_text(String alt_text) {
        this.alt_text = alt_text;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getMime_type() {
        return mime_type;
    }

    public void setMime_type(String mime_type) {
        this.mime_type = mime_type;
    }

    public MediaDetailsBean getMedia_details() {
        return media_details;
    }

    public void setMedia_details(MediaDetailsBean media_details) {
        this.media_details = media_details;
    }

    public int getPost() {
        return post;
    }

    public void setPost(int post) {
        this.post = post;
    }

    public String getSource_url() {
        return source_url;
    }

    public void setSource_url(String source_url) {
        this.source_url = source_url;
    }

    public LinksBean get_links() {
        return _links;
    }

    public void set_links(LinksBean _links) {
        this._links = _links;
    }

    public List<?> getMeta() {
        return meta;
    }

    public void setMeta(List<?> meta) {
        this.meta = meta;
    }

    public static class GuidBean {
        /**
         * rendered : http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0.jpg
         */

        private String rendered;

        public String getRendered() {
            return rendered;
        }

        public void setRendered(String rendered) {
            this.rendered = rendered;
        }
    }

    public static class TitleBean {
        /**
         * rendered : 2011011509_by_macinfish-d377os0
         */

        private String rendered;

        public String getRendered() {
            return rendered;
        }

        public void setRendered(String rendered) {
            this.rendered = rendered;
        }
    }

    public static class DescriptionBean {
        /**
         * rendered : <p class="attachment"><a href='http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0.jpg'><img width="300" height="188" src="http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0-300x188.jpg" class="attachment-medium size-medium" alt="" srcset="http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0-300x188.jpg 300w, http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0-768x480.jpg 768w, http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0-1024x640.jpg 1024w, http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0.jpg 1440w" sizes="(max-width: 300px) 100vw, 300px" /></a></p>

         */

        private String rendered;

        public String getRendered() {
            return rendered;
        }

        public void setRendered(String rendered) {
            this.rendered = rendered;
        }
    }

    public static class CaptionBean {
        /**
         * rendered :
         */

        private String rendered;

        public String getRendered() {
            return rendered;
        }

        public void setRendered(String rendered) {
            this.rendered = rendered;
        }
    }

    public static class MediaDetailsBean {
        /**
         * width : 1440
         * height : 900
         * file : 2019/07/2011011509_by_macinfish-d377os0.jpg
         * sizes : {"thumbnail":{"file":"2011011509_by_macinfish-d377os0-150x150.jpg","width":150,"height":150,"mime_type":"image/jpeg","source_url":"http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0-150x150.jpg"},"medium":{"file":"2011011509_by_macinfish-d377os0-300x188.jpg","width":300,"height":188,"mime_type":"image/jpeg","source_url":"http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0-300x188.jpg"},"medium_large":{"file":"2011011509_by_macinfish-d377os0-768x480.jpg","width":768,"height":480,"mime_type":"image/jpeg","source_url":"http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0-768x480.jpg"},"large":{"file":"2011011509_by_macinfish-d377os0-1024x640.jpg","width":1024,"height":640,"mime_type":"image/jpeg","source_url":"http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0-1024x640.jpg"},"full":{"file":"2011011509_by_macinfish-d377os0.jpg","width":1440,"height":900,"mime_type":"image/jpeg","source_url":"http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0.jpg"}}
         * image_meta : {"aperture":"0","credit":"","camera":"","caption":"","created_timestamp":"0","copyright":"","focal_length":"0","iso":"0","shutter_speed":"0","title":"","orientation":"0","keywords":[]}
         */

        private int width;
        private int height;
        private String file;
        private SizesBean sizes;
        private ImageMetaBean image_meta;

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public SizesBean getSizes() {
            return sizes;
        }

        public void setSizes(SizesBean sizes) {
            this.sizes = sizes;
        }

        public ImageMetaBean getImage_meta() {
            return image_meta;
        }

        public void setImage_meta(ImageMetaBean image_meta) {
            this.image_meta = image_meta;
        }

        public static class SizesBean {
            /**
             * thumbnail : {"file":"2011011509_by_macinfish-d377os0-150x150.jpg","width":150,"height":150,"mime_type":"image/jpeg","source_url":"http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0-150x150.jpg"}
             * medium : {"file":"2011011509_by_macinfish-d377os0-300x188.jpg","width":300,"height":188,"mime_type":"image/jpeg","source_url":"http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0-300x188.jpg"}
             * medium_large : {"file":"2011011509_by_macinfish-d377os0-768x480.jpg","width":768,"height":480,"mime_type":"image/jpeg","source_url":"http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0-768x480.jpg"}
             * large : {"file":"2011011509_by_macinfish-d377os0-1024x640.jpg","width":1024,"height":640,"mime_type":"image/jpeg","source_url":"http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0-1024x640.jpg"}
             * full : {"file":"2011011509_by_macinfish-d377os0.jpg","width":1440,"height":900,"mime_type":"image/jpeg","source_url":"http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0.jpg"}
             */

            private ThumbnailBean thumbnail;
            private MediumBean medium;
            private MediumLargeBean medium_large;
            private LargeBean large;
            private FullBean full;

            public ThumbnailBean getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(ThumbnailBean thumbnail) {
                this.thumbnail = thumbnail;
            }

            public MediumBean getMedium() {
                return medium;
            }

            public void setMedium(MediumBean medium) {
                this.medium = medium;
            }

            public MediumLargeBean getMedium_large() {
                return medium_large;
            }

            public void setMedium_large(MediumLargeBean medium_large) {
                this.medium_large = medium_large;
            }

            public LargeBean getLarge() {
                return large;
            }

            public void setLarge(LargeBean large) {
                this.large = large;
            }

            public FullBean getFull() {
                return full;
            }

            public void setFull(FullBean full) {
                this.full = full;
            }

            public static class ThumbnailBean {
                /**
                 * file : 2011011509_by_macinfish-d377os0-150x150.jpg
                 * width : 150
                 * height : 150
                 * mime_type : image/jpeg
                 * source_url : http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0-150x150.jpg
                 */

                private String file;
                private int width;
                private int height;
                private String mime_type;
                private String source_url;

                public String getFile() {
                    return file;
                }

                public void setFile(String file) {
                    this.file = file;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public String getMime_type() {
                    return mime_type;
                }

                public void setMime_type(String mime_type) {
                    this.mime_type = mime_type;
                }

                public String getSource_url() {
                    return source_url;
                }

                public void setSource_url(String source_url) {
                    this.source_url = source_url;
                }
            }

            public static class MediumBean {
                /**
                 * file : 2011011509_by_macinfish-d377os0-300x188.jpg
                 * width : 300
                 * height : 188
                 * mime_type : image/jpeg
                 * source_url : http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0-300x188.jpg
                 */

                private String file;
                private int width;
                private int height;
                private String mime_type;
                private String source_url;

                public String getFile() {
                    return file;
                }

                public void setFile(String file) {
                    this.file = file;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public String getMime_type() {
                    return mime_type;
                }

                public void setMime_type(String mime_type) {
                    this.mime_type = mime_type;
                }

                public String getSource_url() {
                    return source_url;
                }

                public void setSource_url(String source_url) {
                    this.source_url = source_url;
                }
            }

            public static class MediumLargeBean {
                /**
                 * file : 2011011509_by_macinfish-d377os0-768x480.jpg
                 * width : 768
                 * height : 480
                 * mime_type : image/jpeg
                 * source_url : http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0-768x480.jpg
                 */

                private String file;
                private int width;
                private int height;
                private String mime_type;
                private String source_url;

                public String getFile() {
                    return file;
                }

                public void setFile(String file) {
                    this.file = file;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public String getMime_type() {
                    return mime_type;
                }

                public void setMime_type(String mime_type) {
                    this.mime_type = mime_type;
                }

                public String getSource_url() {
                    return source_url;
                }

                public void setSource_url(String source_url) {
                    this.source_url = source_url;
                }
            }

            public static class LargeBean {
                /**
                 * file : 2011011509_by_macinfish-d377os0-1024x640.jpg
                 * width : 1024
                 * height : 640
                 * mime_type : image/jpeg
                 * source_url : http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0-1024x640.jpg
                 */

                private String file;
                private int width;
                private int height;
                private String mime_type;
                private String source_url;

                public String getFile() {
                    return file;
                }

                public void setFile(String file) {
                    this.file = file;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public String getMime_type() {
                    return mime_type;
                }

                public void setMime_type(String mime_type) {
                    this.mime_type = mime_type;
                }

                public String getSource_url() {
                    return source_url;
                }

                public void setSource_url(String source_url) {
                    this.source_url = source_url;
                }
            }

            public static class FullBean {
                /**
                 * file : 2011011509_by_macinfish-d377os0.jpg
                 * width : 1440
                 * height : 900
                 * mime_type : image/jpeg
                 * source_url : http://asian.dotplays.com/wp-content/uploads/2019/07/2011011509_by_macinfish-d377os0.jpg
                 */

                private String file;
                private int width;
                private int height;
                private String mime_type;
                private String source_url;

                public String getFile() {
                    return file;
                }

                public void setFile(String file) {
                    this.file = file;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public String getMime_type() {
                    return mime_type;
                }

                public void setMime_type(String mime_type) {
                    this.mime_type = mime_type;
                }

                public String getSource_url() {
                    return source_url;
                }

                public void setSource_url(String source_url) {
                    this.source_url = source_url;
                }
            }
        }

        public static class ImageMetaBean {
            /**
             * aperture : 0
             * credit :
             * camera :
             * caption :
             * created_timestamp : 0
             * copyright :
             * focal_length : 0
             * iso : 0
             * shutter_speed : 0
             * title :
             * orientation : 0
             * keywords : []
             */

            private String aperture;
            private String credit;
            private String camera;
            private String caption;
            private String created_timestamp;
            private String copyright;
            private String focal_length;
            private String iso;
            private String shutter_speed;
            private String title;
            private String orientation;
            private List<?> keywords;

            public String getAperture() {
                return aperture;
            }

            public void setAperture(String aperture) {
                this.aperture = aperture;
            }

            public String getCredit() {
                return credit;
            }

            public void setCredit(String credit) {
                this.credit = credit;
            }

            public String getCamera() {
                return camera;
            }

            public void setCamera(String camera) {
                this.camera = camera;
            }

            public String getCaption() {
                return caption;
            }

            public void setCaption(String caption) {
                this.caption = caption;
            }

            public String getCreated_timestamp() {
                return created_timestamp;
            }

            public void setCreated_timestamp(String created_timestamp) {
                this.created_timestamp = created_timestamp;
            }

            public String getCopyright() {
                return copyright;
            }

            public void setCopyright(String copyright) {
                this.copyright = copyright;
            }

            public String getFocal_length() {
                return focal_length;
            }

            public void setFocal_length(String focal_length) {
                this.focal_length = focal_length;
            }

            public String getIso() {
                return iso;
            }

            public void setIso(String iso) {
                this.iso = iso;
            }

            public String getShutter_speed() {
                return shutter_speed;
            }

            public void setShutter_speed(String shutter_speed) {
                this.shutter_speed = shutter_speed;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getOrientation() {
                return orientation;
            }

            public void setOrientation(String orientation) {
                this.orientation = orientation;
            }

            public List<?> getKeywords() {
                return keywords;
            }

            public void setKeywords(List<?> keywords) {
                this.keywords = keywords;
            }
        }
    }

    public static class LinksBean {
        private List<SelfBean> self;
        private List<CollectionBean> collection;
        private List<AboutBean> about;
        private List<AuthorBean> author;
        private List<RepliesBean> replies;

        public List<SelfBean> getSelf() {
            return self;
        }

        public void setSelf(List<SelfBean> self) {
            this.self = self;
        }

        public List<CollectionBean> getCollection() {
            return collection;
        }

        public void setCollection(List<CollectionBean> collection) {
            this.collection = collection;
        }

        public List<AboutBean> getAbout() {
            return about;
        }

        public void setAbout(List<AboutBean> about) {
            this.about = about;
        }

        public List<AuthorBean> getAuthor() {
            return author;
        }

        public void setAuthor(List<AuthorBean> author) {
            this.author = author;
        }

        public List<RepliesBean> getReplies() {
            return replies;
        }

        public void setReplies(List<RepliesBean> replies) {
            this.replies = replies;
        }

        public static class SelfBean {
            /**
             * href : http://asian.dotplays.com/wp-json/wp/v2/media/919
             */

            private String href;

            public String getHref() {
                return href;
            }

            public void setHref(String href) {
                this.href = href;
            }
        }

        public static class CollectionBean {
            /**
             * href : http://asian.dotplays.com/wp-json/wp/v2/media
             */

            private String href;

            public String getHref() {
                return href;
            }

            public void setHref(String href) {
                this.href = href;
            }
        }

        public static class AboutBean {
            /**
             * href : http://asian.dotplays.com/wp-json/wp/v2/types/attachment
             */

            private String href;

            public String getHref() {
                return href;
            }

            public void setHref(String href) {
                this.href = href;
            }
        }

        public static class AuthorBean {
            /**
             * embeddable : true
             * href : http://asian.dotplays.com/wp-json/wp/v2/users/1
             */

            private boolean embeddable;
            private String href;

            public boolean isEmbeddable() {
                return embeddable;
            }

            public void setEmbeddable(boolean embeddable) {
                this.embeddable = embeddable;
            }

            public String getHref() {
                return href;
            }

            public void setHref(String href) {
                this.href = href;
            }
        }

        public static class RepliesBean {
            /**
             * embeddable : true
             * href : http://asian.dotplays.com/wp-json/wp/v2/comments?post=919
             */

            private boolean embeddable;
            private String href;

            public boolean isEmbeddable() {
                return embeddable;
            }

            public void setEmbeddable(boolean embeddable) {
                this.embeddable = embeddable;
            }

            public String getHref() {
                return href;
            }

            public void setHref(String href) {
                this.href = href;
            }
        }
    }
}
