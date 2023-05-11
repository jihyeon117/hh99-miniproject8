package com.example.hh99miniproject8.config;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.CharacterEscapes;
import com.fasterxml.jackson.core.io.SerializedString;
import org.apache.commons.text.StringEscapeUtils;

// JSON 응답의 특수 문자를 이스케이프하여 청XSS 공격을 방지
// CharacterEscapes : JSON 문자열에서 문자를 이스케이프하는 방법을 정의하는 Jackson 클래스
public class HtmlCharacterEscapes extends CharacterEscapes {

    private final int[] asciiEscapes;

    // 사용자 지정 이스케이프 시퀀스로 대체해야 하는 ASCII 이스케이프 코드 배열을 설정
    public HtmlCharacterEscapes() {
        // XSS 방지 처리할 특수 문자 지정
        asciiEscapes = CharacterEscapes.standardAsciiEscapesForJSON();
        asciiEscapes['<'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['>'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\"'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['('] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes[')'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['#'] = CharacterEscapes.ESCAPE_CUSTOM;
        asciiEscapes['\''] = CharacterEscapes.ESCAPE_CUSTOM;
    }

    // 생성자에 설정된 ASCII 이스케이프 코드 배열을 반환합니다.
    @Override
    public int[] getEscapeCodesForAscii() {
        return asciiEscapes;
    }

    // 지정된 문자의 사용자 지정 이스케이프 시퀀스를 나타내는 SerializableString 개체를 반환
    // Apache Commons Lang 라이브러리의 StringEscapeUtils.escapeHtml4() 메서드를 사용하여 문자를 HTML로 이스케이프 한다.
    @Override
    public SerializableString getEscapeSequence(int ch) {
        return new SerializedString(StringEscapeUtils.escapeHtml4(Character.toString((char) ch)));
    }
}
