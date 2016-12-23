package bxlx.graphics;

import java.util.HashMap;
import java.util.function.Function;

/**
 * Created by qqcs on 2016.12.23..
 */
public class ImageCaches<ImageType> {
    private final HashMap<String, ImageType> images = new HashMap<>();

    private final Function<String, ImageType> creator;


    public ImageCaches(Function<String, ImageType> creator) {
        this.creator = creator;
    }

    public ImageType get(String src) {
        ImageType result = images.get(src);
        if(result == null) {
            result = create(src);
        }
        return result;
    }

    public ImageType create(String src) {
        ImageType result = creator.apply(src);
        images.remove(src);
        images.put(src, result);
        return result;
    }
}
