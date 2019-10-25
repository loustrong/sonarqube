/*
 * SonarQube
 * Copyright (C) 2009-2019 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.server.almsettings;

import org.sonar.api.server.ws.Request;
import org.sonar.api.server.ws.Response;
import org.sonar.api.server.ws.WebService;
import org.sonar.db.DbClient;
import org.sonar.db.DbSession;
import org.sonar.db.alm.setting.AlmSettingDto;
import org.sonar.server.user.UserSession;

import static org.apache.commons.lang.StringUtils.isNotBlank;

public class UpdateAzureAction implements AlmSettingsWsAction {

  private static final String PARAM_KEY = "key";
  private static final String PARAM_NEW_KEY = "newKey";
  private static final String PARAM_PERSONAL_ACCESS_TOKEN = "personalAccessToken";

  private final DbClient dbClient;
  private UserSession userSession;
  private final AlmSettingsSupport almSettingsSupport;

  public UpdateAzureAction(DbClient dbClient, UserSession userSession, AlmSettingsSupport almSettingsSupport) {
    this.dbClient = dbClient;
    this.userSession = userSession;
    this.almSettingsSupport = almSettingsSupport;
  }

  @Override
  public void define(WebService.NewController context) {
    WebService.NewAction action = context.createAction("update_azure")
      .setDescription("Update Azure ALM instance Setting. <br/>" +
        "Requires the 'Administer System' permission")
      .setPost(true)
      .setSince("8.1")
      .setHandler(this);

    action.createParam(PARAM_KEY)
      .setRequired(true)
      .setMaximumLength(200)
      .setDescription("Unique key of the Azure instance setting");
    action.createParam(PARAM_NEW_KEY)
      .setRequired(false)
      .setMaximumLength(200)
      .setDescription("Optional new value for an unique key of the Azure Devops instance setting");
    action.createParam(PARAM_PERSONAL_ACCESS_TOKEN)
      .setRequired(true)
      .setMaximumLength(2000)
      .setDescription("Azure Devops personal access token");
  }

  @Override
  public void handle(Request request, Response response) throws Exception {
    userSession.checkIsSystemAdministrator();
    doHandle(request);
    response.noContent();
  }

  private void doHandle(Request request) {
    String key = request.mandatoryParam(PARAM_KEY);
    String newKey = request.param(PARAM_NEW_KEY);
    String pat = request.mandatoryParam(PARAM_PERSONAL_ACCESS_TOKEN);

    try (DbSession dbSession = dbClient.openSession(false)) {
      AlmSettingDto almSettingDto = almSettingsSupport.getAlmSetting(dbSession, key);
      if (isNotBlank(newKey) && !newKey.equals(key)) {
        almSettingsSupport.checkAlmSettingDoesNotAlreadyExist(dbSession, newKey);
      }
      dbClient.almSettingDao().update(dbSession, almSettingDto
        .setKey(isNotBlank(newKey) ? newKey : key)
        .setPersonalAccessToken(pat));
      dbSession.commit();
    }
  }

}
