/****************************************************************************
**
** Copyright (C) 2021 Equo
**
** This file is part of the Equo SDK.
**
** Commercial License Usage
** Licensees holding valid commercial Equo licenses may use this file in
** accordance with the commercial license agreement provided with the
** Software or, alternatively, in accordance with the terms contained in
** a written agreement between you and Equo. For licensing terms
** and conditions see https://www.equo.dev/terms.
**
** GNU General Public License Usage
** Alternatively, this file may be used under the terms of the GNU
** General Public License version 3 as published by the Free Software
** Foundation. Please review the following
** information to ensure the GNU General Public License requirements will
** be met: https://www.gnu.org/licenses/gpl-3.0.html.
**
****************************************************************************/

package com.equo.contribution.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.osgi.framework.Bundle;

import com.equo.contribution.api.EquoContribution;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class EquoContributionConfigServiceTest {

	@Ignore
	@Test
	public void testDefineContributionsBasicCase() {
		EquoContributionConfigService configService = new EquoContributionConfigService();
		Bundle bundle = null;
		JsonParser parser = new JsonParser();
		JsonElement configJson = parser.parse("{" + "	\"contributions\" : 	["
				+ "						  {" + "			"
				+ "							\"contributionName\" : \"contribution\","
				+ "							"
				+ "							\"contributionHtmlName\" : \"example.html\","
				+ "							"
				+ "							\"contributedScripts\" : [\"script4.js\", \"script5.js\",\"script6.js\"],"
				+ "							"
				+ "							\"proxiedUris\" : [\"uri3\",\"uri4\"],"
				+ "							"
				+ "							\"pathsWithScripts\": {	\"path4\" : \"script4.js\","
				+ "													\"path5\" : \"script5.js\","
				+ "													\"path6\" : \"script6.js\""
				+ "												}" + "						  }" + "	"
				+ "						]" + "}");
		List<EquoContribution> contributions = configService.defineContributions(configJson.getAsJsonObject(), bundle);
		assertThat(contributions).hasSize(1);
		assertThat(contributions.get(0).getContributionName()).isEqualTo("contribution");
		assertThat(contributions.get(0).getContributedResourceName()).isEqualTo("example.html");
		assertThat(contributions.get(0).getContributedScripts()).hasSize(3);
		assertThat(contributions.get(0).getProxiedUris()).hasSize(2);
		assertThat(contributions.get(0).getPathsToScripts().keySet()).hasSize(3);
	}

	@Ignore
	@Test
	public void testDefineContributionsCompoundCase() {
		EquoContributionConfigService configService = new EquoContributionConfigService();
		Bundle bundle = null;
		JsonParser parser = new JsonParser();
		JsonElement configJson = parser.parse("{" + "	\"contributions\" : 	["
				+ "							{" + "			"
				+ "							\"contributionName\" : \"Contribution1\","
				+ "							"
				+ "							\"contributionHtmlName\" : \"example.html\","
				+ "							"
				+ "							\"contributedScripts\" : [\"script1.js\", \"script2.js\",\"script3.js\"],"
				+ "							"
				+ "							\"proxiedUris\" : [\"uri1\",\"uri2\"],"
				+ "							"
				+ "							\"pathsWithScripts\": {	\"path4\" : \"script4.js\","
				+ "													\"path5\" : \"script5.js\","
				+ "													\"path6\" : \"script6.js\""
				+ "												}" + "						  },"
				+ "						  {" + "			"
				+ "							\"contributionName\" : \"Contribution2\","
				+ "							"
				+ "							\"contributionHtmlName\" : \"example2.html\","
				+ "							"
				+ "							\"contributedScripts\" : [\"script4.js\", \"script5.js\",\"script6.js\"],"
				+ "							"
				+ "							\"proxiedUris\" : [\"uri3\",\"uri4\"],"
				+ "							"
				+ "							\"pathsWithScripts\": {	\"path4\" : \"script4.js\","
				+ "													\"path5\" : \"script5.js\","
				+ "													\"path6\" : \"script6.js\""
				+ "												}" + "						  }" + "	"
				+ "						]" + "}");
		List<EquoContribution> contributions = configService.defineContributions(configJson.getAsJsonObject(), bundle);
		assertThat(contributions).hasSize(2);
		assertThat(contributions.get(0).getContributionName()).isEqualTo("contribution1");
		assertThat(contributions.get(0).getContributedResourceName()).isEqualTo("example.html");
		assertThat(contributions.get(0).getContributedScripts()).hasSize(3);
		assertThat(contributions.get(0).getProxiedUris()).hasSize(2);
		assertThat(contributions.get(0).getPathsToScripts().keySet()).hasSize(3);

		assertThat(contributions.get(1).getContributionName()).isEqualTo("contribution2");
		assertThat(contributions.get(1).getContributedResourceName()).isEqualTo("example2.html");
		assertThat(contributions.get(1).getContributedScripts()).hasSize(3);
		assertThat(contributions.get(1).getProxiedUris()).hasSize(2);
		assertThat(contributions.get(1).getPathsToScripts().keySet()).hasSize(3);

	}

	@Ignore
	@Test
	public void testDefineContributionsCompoundCaseMissingFields() {
		EquoContributionConfigService configService = new EquoContributionConfigService();
		Bundle bundle = null;
		JsonParser parser = new JsonParser();
		JsonElement configJson = parser.parse("{" + "	\"contributions\" : 	["
				+ "							{" + "			"
				+ "							\"contributionHtmlName\" : \"example.html\","
				+ "							"
				+ "							\"contributedScripts\" : [\"script1.js\", \"script2.js\",\"script3.js\"],"
				+ "							"
				+ "							\"proxiedUris\" : [\"uri1\",\"uri2\"],"
				+ "							"
				+ "							\"pathsWithScripts\": {	\"path4\" : \"script4.js\","
				+ "													\"path5\" : \"script5.js\","
				+ "													\"path6\" : \"script6.js\""
				+ "												}" + "						  },"
				+ "						  {" + "			"
				+ "							\"contributionName\" : \"Contribution2\","
				+ "							"
				+ "							\"contributedScripts\" : [\"script4.js\", \"script5.js\",\"script6.js\"],"
				+ "							"
				+ "							\"proxiedUris\" : [\"uri3\",\"uri4\"],"
				+ "							"
				+ "							\"pathsWithScripts\": {	\"path4\" : \"script4.js\","
				+ "													\"path5\" : \"script5.js\","
				+ "													\"path6\" : \"script6.js\""
				+ "												}" + "						  },"
				+ "						  {" + "			"
				+ "							\"contributionName\" : \"Contribution2\","
				+ "							"
				+ "							\"contributionHtmlName\" : \"example2.html\","
				+ "							"
				+ "							\"proxiedUris\" : [\"uri3\",\"uri4\"],"
				+ "							"
				+ "							\"pathsWithScripts\": {	\"path4\" : \"script4.js\","
				+ "													\"path5\" : \"script5.js\","
				+ "													\"path6\" : \"script6.js\""
				+ "												}" + "						  },"
				+ "						  {" + "			"
				+ "							\"contributionName\" : \"Contribution2\","
				+ "							"
				+ "							\"contributionHtmlName\" : \"example2.html\","
				+ "							"
				+ "							\"contributedScripts\" : [\"script4.js\", \"script5.js\",\"script6.js\"],"
				+ "							"
				+ "							\"pathsWithScripts\": {	\"path4\" : \"script4.js\","
				+ "													\"path5\" : \"script5.js\","
				+ "													\"path6\" : \"script6.js\""
				+ "												}" + "						  },"
				+ "						  {" + "			"
				+ "							\"contributionName\" : \"Contribution2\","
				+ "							"
				+ "							\"contributionHtmlName\" : \"example2.html\","
				+ "							"
				+ "							\"contributedScripts\" : [\"script4.js\", \"script5.js\",\"script6.js\"],"
				+ "							"
				+ "							\"proxiedUris\" : [\"uri3\",\"uri4\"]" + "						  }"
				+ "	" + "						]" + "}");
		List<EquoContribution> contributions = configService.defineContributions(configJson.getAsJsonObject(), bundle);

		assertThat(contributions).hasSize(5);

		// checkea el valor por defecto que el builder le asigna a las contribuciones a
		// las cuales no se les asigna nombre.
		assertThat(contributions.get(0).getContributionName()).contains("equocontribution");

		// checkea el valor por defecto que el builder le asigna a las contribuciones a
		// las cuales no se les asigna html.
		assertThat(contributions.get(1).getContributedResourceName()).isEqualTo("");

		// checkea que no se hayan definido scripts
		assertThat(contributions.get(2).getContributedScripts()).hasSize(0);

		// checkea que no se hayan definido proxiedUris
		assertThat(contributions.get(3).getProxiedUris()).hasSize(0);

		// checkea que no se hayan definido paths con scripts
		assertThat(contributions.get(4).getPathsToScripts().keySet()).hasSize(0);

	}

	@Test(expected = RuntimeException.class)
	public void testDefineContributionsExceptionCase() {
		EquoContributionConfigService configService = new EquoContributionConfigService();
		Bundle bundle = null;
		JsonParser parser = new JsonParser();
		JsonElement configJson = parser
				.parse("{" + "	\"contributions\" : 	[" + "						  {"
						+ "						  }" + "	" + "						]" + "}");
		configService.defineContributions(configJson.getAsJsonObject(), bundle);

	}

	@Test(expected = RuntimeException.class)
	public void testDefineContributionsExceptionCase2() {
		EquoContributionConfigService configService = new EquoContributionConfigService();
		Bundle bundle = null;
		JsonParser parser = new JsonParser();
		JsonElement configJson = parser.parse("{" + "	\"contributions\" : 	["
				+ "							{" + "			"
				+ "							\"contributionName\" : \"Contribution1\","
				+ "							"
				+ "							\"contributionHtmlName\" : \"example.html\","
				+ "							"
				+ "							\"contributedScripts\" : [\"script1.js\", \"script2.js\",\"script3.js\"],"
				+ "							"
				+ "							\"proxiedUris\" : [\"uri1\",\"uri2\"],"
				+ "							"
				+ "							\"pathsWithScripts\": {	\"path4\" : \"script4.js\","
				+ "													\"path5\" : \"script5.js\","
				+ "													\"path6\" : \"script6.js\""
				+ "												}" + "						  },"
				+ "						  {" + "						  }" + "	"
				+ "						]" + "}");
		configService.defineContributions(configJson.getAsJsonObject(), bundle);

	}

}
