declare namespace Model.Security {
  /**
   * "id": 10001,
   *         "creator": null,
   *         "createTime": "2024-01-28T00:39:52",
   *         "updater": null,
   *         "updateTime": "2024-01-28T00:39:52",
   *         "code": "20240128003951",
   *         "agencyCode": 10000,
   *         "username": "admin",
   *         "nickname": "普通用户",
   *         "name": "张三",
   *         "introduction": "",
   *         "avatar": "https://avatars.githubusercontent.com/u/63219216?v=4",
   *         "gender": 0,
   *         "birthday": "2024-01-28",
   *         "phone": "",
   *         "email": "zolo@onezol.com",
   *         "roles": null,
   *         "permissions": null,
   *         "status": 0
   */
  type User = {
    id: number;
    code: string;
    username: string;
    nickname: string;
    name: string;
    introduction:string;
    gender: number;
    birthday: string;
    phone:string
    email: string;
    password: string;
    avatar: string;
    status: number;
    createTime: string;
    updateTime: string;
  }

  type JWT = {
    token: string;
    expire: number;
  }
}

declare namespace Request.Security {
  type UserLogin = {
    username: string;
    email: string;
    password: string;
    verifyCode: string;
  }
}

declare namespace Response.Security {
  type UserAuthentication = {
    user: Model.Security.User;
    jwt: Model.Security.JWT;
  }
}
