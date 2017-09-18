package com.selaliadobor.githubeventbrowser.layout;


import android.text.Layout;

import com.facebook.litho.Column;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.ComponentLayout;
import com.facebook.litho.Row;
import com.facebook.litho.annotations.LayoutSpec;
import com.facebook.litho.annotations.OnCreateLayout;
import com.facebook.litho.annotations.Prop;
import com.facebook.litho.widget.Text;
import com.facebook.yoga.YogaAlign;
import com.facebook.yoga.YogaEdge;
import com.github.pavlospt.litho.glide.GlideImage;
import com.selaliadobor.githubeventbrowser.githubapi.responseobjects.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static humanize.Humanize.naturalTime;

/**
 * Defines a layout for displaying Github Events
 */
@LayoutSpec
public class EventListItemLayoutSpec {

    public static final SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    @OnCreateLayout
    static ComponentLayout onCreateLayout(
            ComponentContext c,
            @Prop Event event) {
        String eventCreationDate = getReadableEventCreationDate(event);

        ComponentLayout textColumn = Column.create(c)
                .marginDip(YogaEdge.LEFT, 16)
                .child(
                        Text.create(c)
                                .text(event.type())
                                .textSizeSp(18)
                                .withLayout()
                                .flexGrow(1)
                )
                .child(
                        Text.create(c)
                                .text(eventCreationDate)
                                .textSizeSp(14)
                )
                .widthPercent(60)
                .alignContent(YogaAlign.CENTER)
                .build();
        return Row.create(c)
                .heightDip(75)
                .child(

                        Column.create(c)
                                .marginDip(YogaEdge.LEFT, 16)
                                .child(
                                        GlideImage.create(c)
                                                .imageUrl(event.actor().avatarUrl())
                                                .withLayout()
                                                .marginDip(YogaEdge.ALL, 1)
                                                .flexGrow(1)
                                                .heightPercent(70)
                                )
                                .child(
                                        Text.create(c)
                                                .text(event.actor().login())
                                                .textSizeSp(14)
                                                .textAlignment(Layout.Alignment.ALIGN_CENTER)
                                )
                                .widthPercent(40)
                                .alignContent(YogaAlign.CENTER)
                                .build()
                )
                .child(textColumn)
                .build();
    }

    private static String getReadableEventCreationDate(@Prop Event event) {
        try {
            ISO_DATE_FORMAT.setTimeZone(UTC);
            Date parsedDate = ISO_DATE_FORMAT.parse(event.createdAt());
            return naturalTime(parsedDate);
        } catch (ParseException e) {
            return event.createdAt();
        }
    }
}
