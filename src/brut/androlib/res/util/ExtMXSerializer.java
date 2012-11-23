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

package brut.androlib.res.util;

import org.xmlpull.mxp1_serializer.MXSerializer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * @author Ryszard Wiśniewski <brut.alll@gmail.com>
 */
public class ExtMXSerializer extends MXSerializer implements ExtXmlSerializer {
    @Override
    public void startDocument(String encoding, Boolean standalone) throws
            IOException, IllegalArgumentException, IllegalStateException {
        super.startDocument(encoding != null ? encoding : mDefaultEncoding,
                standalone);
        this.newLine();
    }

    @Override
    protected void writeAttributeValue(String value, Writer out)
            throws IOException {
        if (mIsDisabledAttrEscape) {
            out.write(value);
            return;
        }
        super.writeAttributeValue(value, out);
    }

    @Override
    public void setOutput(OutputStream os, String encoding) throws IOException {
        super.setOutput(os, encoding != null ? encoding : mDefaultEncoding);
    }

    @Override
    public Object getProperty(String name) throws IllegalArgumentException {
        if (PROPERTY_DEFAULT_ENCODING.equals(name)) {
            return mDefaultEncoding;
        }
        return super.getProperty(name);
    }

    @Override
    public void setProperty(String name, Object value)
            throws IllegalArgumentException, IllegalStateException {
        if (PROPERTY_DEFAULT_ENCODING.equals(name)) {
            mDefaultEncoding = (String) value;
        } else {
            super.setProperty(name, value);
        }
    }

    public ExtXmlSerializer newLine() throws IOException {
        super.out.write(lineSeparator);
        return this;
    }

    public void setDisabledAttrEscape(boolean disabled) {
        mIsDisabledAttrEscape = disabled;
    }

    private String mDefaultEncoding;
    private boolean mIsDisabledAttrEscape = false;

}
