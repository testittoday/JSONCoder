/*************************************************************
 Copyright 2018-2019 eBay Inc.
 Author/Developer: Jianwu Chen

 Use of this source code is governed by an MIT-style
 license that can be found in the LICENSE file or at
 https://opensource.org/licenses/MIT.
 ************************************************************/

package com.ebay.jsoncoder.treedoc;

import com.ebay.jsoncodercore.factory.Function;
import com.ebay.jsoncodercore.factory.InjectableFactory;
import com.ebay.jsoncodercore.type.Predicator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArrayCharSource extends CharSource {
  public final static InjectableFactory<char[], ArrayCharSource> factory = InjectableFactory.of(new Function<char[], ArrayCharSource>() {
    @Override public ArrayCharSource apply(char[] param) { return new ArrayCharSource(param); }
  });

  final char[] buf;
  final int startIndex;
  final int endIndex;

  public ArrayCharSource(char[] buff) { this(buff, 0, buff.length); }

  @Override public char read() {
    if (isEof(0))
      throw new EOFRuntimeException();
    return bookmark.append(buf[startIndex + bookmark.pos]);
  }

  @Override public char peek(int i) {
    if (isEof(i))
      throw new EOFRuntimeException();
    return buf[startIndex + bookmark.pos + i];
  }

  @Override public boolean isEof(int i) { return startIndex + bookmark.pos + i >= endIndex; }

  @Override public boolean readUntil(int length, Predicator<CharSource> predicator, StringBuilder target) {
    int startPos = bookmark.pos;
    int len = 0;
    boolean matched = false;
    for (; len < length && !(isEof(0)); len++) {
      matched = predicator.test(this);
      if (matched)
        break;
      read();
    }
    if (target != null)
      target.append(buf, startIndex + startPos, len);
    return matched;
  }
}
