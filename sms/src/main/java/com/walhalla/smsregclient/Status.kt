package com.walhalla.smsregclient

enum class Status(@kotlin.jvm.JvmField val value: String) {
    TZ_INPOOL("TZ_INPOOL"),
    TZ_NUM_PREPARE("TZ_NUM_PREPARE"),
    TZ_NUM_WAIT("TZ_NUM_WAIT"),
    TZ_NUM_ANSWER("TZ_NUM_ANSWER"),

    //timeout
    TZ_OVER_NR("TZ_OVER_NR"),

    TZ_OVER_EMPTY("TZ_OVER_EMPTY"),

    TZ_OVER_OK("TZ_OVER_OK"),

    //other
    TZ_NUM_WAIT2("TZ_NUM_WAIT2"),
    TZ_NUM_ANSWER2("TZ_NUM_ANSWER2"),
    WARNING_NO_NUMS("WARNING_NO_NUMS"),
    TZ_OVER_GR("TZ_OVER_GR"),

    TZ_OVER2_EMPTY("TZ_OVER2_EMPTY"),
    TZ_OVER2_OK("TZ_OVER2_OK"),
    TZ_DELETED("TZ_DELETED"),

    ERROR_NO_KEY("ERROR_NO_KEY")
}
