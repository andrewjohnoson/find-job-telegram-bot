package com.zhevlakov.findjobtelegrambot.vacancy.query;

import com.zhevlakov.findjobtelegrambot.user.query.UserQuery;

public interface QueryBuilder {
    String buildQuery(UserQuery userQuery);

}
