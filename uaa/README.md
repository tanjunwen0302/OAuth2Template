## 分布式认证授权服务中心

---
为外界的服务提供身认证，权限校验
<br>
使用对称加密方式，本例允许oauth2授权码和密码两种模式
<br>
使用jwt令牌
<br>
从数据库中读取客户端信息和用户信息
<br>
---

#### 使用时需要先向 */uaa/oauth/token*认证，在此先使用密码模式，需要带上的参数如下：

默认表单提交，在示例中尝试修改为json

- client_id *客户端id*
- client_secret *客户端密钥*
- grant_type *认证类型，密码模式为 password*
- username *用户名*
- password *用户密码*

---

#### 在 */uaa/oauth/check_token* 中可以校验token正确与否<br>

默认表单提交，在示例中尝试json（看了一天没改成，不改了），参数如下

- token *token值*

---

#### 得到token后在后续的每一个请求中需要在Header中携带token值

<br>
Authorization:Bearer token值

---

## 数据库表

- oauth_client_details *客户端配置表，spring oauth2官方给的有模板，不可更改*

| 字段                      | 类型      | 描述                                                                                                  |
|-------------------------|---------|-----------------------------------------------------------------------------------------------------|
| client_id               | varchar | 必填，客户端标识                                                                                            |
| resource_ids            | varchar | 可选，资源id集合，多个资源用英文逗号隔开                                                                               |
| client_secret           | varchar | 必填，Oauth2 client_secret，客户端密钥,保存的密码是加密后的                                                            |
| scope                   | varchar | 必填，Oauth2 权限范围，比如 read，write等可自定义                                                                   |
| authorized_grant_types  | varchar | 必填，Oauth2 授权类型，支持类型：authorization_code,password,refresh_token,implicit,client_credentials，多个用英文逗号隔开 |
| web_server_redirect_uri | varchar | 可选，客户端的重定向URI,当grant_type为authorization_code或implicit时,此字段是需要的                                      |
| authorities             | varchar | 可选，指定客户端所拥有的Spring Security的权限值                                                                     |
| access_token_validity   | int     | 可选，access_token的有效时间值(单位:秒)，不填写框架(类refreshTokenValiditySeconds)默认12小时                               |
| refresh_token_validity  | int     | 可选，refresh_token的有效时间值(单位:秒)，不填写框架(类refreshTokenValiditySeconds)默认30天                               |
| additional_information  | varchar | 预留字段，格式必须是json                                                                                      |
| autoapprove             | varchar | 该字段适用于grant_type="authorization_code"的情况下，用户是否自动approve操作                                           |

#### 其它的表可以自定义，必须能提供 *username*,*password* (加密过的),和可访问的权限三个字段即可
- 示例服务端口5030


