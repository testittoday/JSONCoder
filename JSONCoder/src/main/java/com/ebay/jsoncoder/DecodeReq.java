/*************************************************************
 Copyright 2018-2019 eBay Inc.
 Author/Developer: Jianwu Chen

 Use of this source code is governed by an MIT-style
 license that can be found in the LICENSE file or at
 https://opensource.org/licenses/MIT.
 ************************************************************/

package com.ebay.jsoncoder;

import com.ebay.jsoncoder.treedoc.ArrayCharSource;
import com.ebay.jsoncoder.treedoc.CharSource;
import com.ebay.jsoncoder.treedoc.ReaderCharSource;
import com.ebay.jsoncoder.treedoc.TDNode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Reader;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Decode Request, the reason we use abstract class, is to force to create a sub-class so that it's possible to get
 * the getActualTypeArguments. If generic type is of the concern, use factory method "of".
 *
 * <p> It can be used to specify source of the JSON document, either through a String, Reader, or a a wrapped CharSource. As
 *
 */
@SuppressWarnings("WeakerAccess")
@Accessors(chain=true)
public abstract class DecodeReq<T> {
  @Setter private Type type;
  @SuppressWarnings("Lombok")
  @Getter @Setter CharSource source;
  @Getter @Setter TDNode jsonNode;
  @Getter @Setter T target;

  public DecodeReq<T> setSource(Reader reader) {
    source = reader == null ? null : new ReaderCharSource(reader);
    return this;
  }
  public DecodeReq<T> setSource(String jsonStr) {
    source = jsonStr == null ? null : new ArrayCharSource(jsonStr.toCharArray());
    return this;
  }

  public Type getType() {
    if (type == null) {
      Type superClass = getClass().getGenericSuperclass();
      type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }
    return type;
  }
  
  public DecodeReq(Type type) { this.type = type;}
  public DecodeReq() { }
  public static <T> DecodeReq<T> of(Type type) { return new DecodeReq<T>(type){}; }
}
