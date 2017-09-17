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

/**
 * Defines a layout for displaying Github Events
 */
@LayoutSpec
public class EventListItemLayoutSpec {

    @OnCreateLayout
    static ComponentLayout onCreateLayout(
            ComponentContext c,
            @Prop Event event) {
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
                                .text(event.createdAt())
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
                                                .marginDip(YogaEdge.ALL,1)
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
}
