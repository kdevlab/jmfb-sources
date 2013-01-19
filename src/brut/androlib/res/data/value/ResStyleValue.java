/**
 *  Copyright 2011 Ryszard Wiśniewski <brut.alll@gmail.com>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package brut.androlib.res.data.value;

import brut.androlib.AndrolibException;
import brut.androlib.res.data.ResResSpec;
import brut.androlib.res.data.ResResource;
import brut.androlib.res.xml.ResValuesXmlSerializable;
import brut.util.Duo;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

/**
 * @author Ryszard Wiśniewski <brut.alll@gmail.com>
 */
public class ResStyleValue extends ResBagValue implements ResValuesXmlSerializable {
    ResStyleValue(ResReferenceValue parent,
                  Duo<Integer, ResScalarValue>[] items, ResValueFactory factory) {
        super(parent);

        mItems = new Duo[items.length];
        for (int i = 0; i < items.length; i++) {
            mItems[i] = new Duo<ResReferenceValue, ResScalarValue>(
                    factory.newReference(items[i].m1, null), items[i].m2);
        }
    }

    @Override
    public void serializeToResValuesXml(XmlSerializer serializer, ResResource res)
            throws IOException, AndrolibException {
        serializer.startTag(null, "style");
        serializer.attribute(null, "name", res.getResSpec().getName());
        if (!mParent.isNull()) {
            serializer.attribute(null, "parent", mParent.encodeAsResXmlAttr());
        }
        for (int i = 0; i < mItems.length; i++) {
            ResResSpec spec = mItems[i].m1.getReferent();

            // fix for ClassCastException by Alsan Wong <alsan.wong@gmail.com> at 2013/01/18 15:08
            ResAttr attr;
            String value = null;

            try {
                // hacky-fix remove bad ReferenceVars
                ResValue attrValue = spec.getDefaultResource().getValue();
                value = attrValue.toString();	// I know, this is not good, but I don't want another variable just for exception handling

                if(value.contains("ResReferenceValue@")) {
                    continue;
                }

                attr = (ResAttr)attrValue;
                value = attr.convertToResXmlFormat(mItems[i].m2);
            } catch(ClassCastException e) {
                System.out.println("ClassCastException: " + e.getMessage());
                System.out.println(value);
            }

            if (value == null) {
                value = mItems[i].m2.encodeAsResXmlValue();
            }

            if (value == null) {
                continue;
            }

            serializer.startTag(null, "item");
            serializer.attribute(null, "name",
                    spec.getFullName(res.getResSpec().getPackage(), true));
            serializer.text(value);
            serializer.endTag(null, "item");
        }
        serializer.endTag(null, "style");
    }


    private final Duo<ResReferenceValue, ResScalarValue>[] mItems;
}
