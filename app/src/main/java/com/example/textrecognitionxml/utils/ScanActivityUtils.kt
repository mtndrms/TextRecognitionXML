package com.example.textrecognitionxml.utils

import androidx.core.graphics.createBitmap

object ScanActivityUtils {
    var extractedText = ""
    var bitmap = createBitmap(1, 1)
    var detectedLanguage: String? = null

    var languages: HashMap<String, String> = hashMapOf(
        "af" to "Afrikaans",
        "ar" to "Arabic",
        "be" to "Belarusian",
        "bg" to "Bulgarian",
        "bn" to "Bengali",
        "ca" to "Catalan",
        "cs" to "Czech",
        "cy" to "Welsh",
        "da" to "Danish",
        "de" to "German",
        "el" to "Greek",
        "en" to "English",
        "eo" to "Esperanto",
        "es" to "Spanish",
        "et" to "Estonian",
        "fa" to "Persian",
        "fi" to "Finnish",
        "fr" to "French",
        "ga" to "Irish",
        "gl" to "Galician",
        "gu" to "Gujarati",
        "he" to "Hebrew",
        "hi" to "Hindi",
        "hr" to "Croatian",
        "ht" to "Haitian",
        "hu" to "Hungarian",
        "id" to "Indonesian",
        "is" to "Icelandic",
        "it" to "Italian",
        "ja" to "Japanese",
        "ka" to "Georgian",
        "kn" to "Kannada",
        "ko" to "Korean",
        "lt" to "Lithuanian",
        "lv" to "Latvian",
        "mk" to "Macedonian",
        "mr" to "Marathi",
        "ms" to "Malay",
        "mt" to "Maltese",
        "nl" to "Dutch",
        "no" to "Norwegian",
        "pl" to "Polish",
        "pt" to "Portuguese",
        "ro" to "Romanian",
        "ru" to "Russian",
        "sk" to "Slovak",
        "sl" to "Slovenian",
        "sq" to "Albanian",
        "sv" to "Swedish",
        "sw" to "Swahili",
        "ta" to "Tamil",
        "te" to "Telugu",
        "th" to "Thai",
        "tl" to "Tagalog",
        "tr" to "Turkish",
        "uk" to "Ukrainian",
        "ur" to "Urdu",
        "vi" to "Vietnamese",
        "zh" to "Chinese"
    )

    var languageCodes = languages.keys.toList().sorted()
    var languageNames = languages.values.toList().sorted()
}