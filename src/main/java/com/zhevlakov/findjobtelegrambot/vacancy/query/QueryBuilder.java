package com.zhevlakov.findjobtelegrambot.vacancy.query;

import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;

import java.net.URI;

public interface QueryBuilder {
    URI buildQuery(UserQuery userQuery);

}
