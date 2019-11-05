package com.make.equo.server.contribution.configservice;

import static org.junit.Assert.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.osgi.framework.Bundle;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.make.equo.server.contribution.EquoContribution;

public class EquoContributionConfigServiceTest {

	@Test
	public void testDefineContributions() {
		EquoContributionConfigService configService = new EquoContributionConfigService();
		Bundle bundle = null;
		JsonParser parser = new JsonParser();
		JsonElement configJson = parser.parse("{\r\n" + 
				"	\"contributions\" : 	[\r\n" + 
				"							{\r\n" + 
				"			\r\n" + 
				"							\"contributionName\" : \"Contribution1\",\r\n" + 
				"							\r\n" + 
				"							\"contributionHtmlName\" : \"example.html\",\r\n" + 
				"							\r\n" + 
				"							\"contributedScripts\" : [\"script1.js\", \"script2.js\",\"script3.js\"],\r\n" + 
				"							\r\n" + 
				"							\"proxiedUris\" : [\"uri1\",\"uri2\"],\r\n" + 
				"							\r\n" + 
				"							\"pathsWithScripts\": {	\"path4\" : \"script4.js\",\r\n" + 
				"													\"path5\" : \"script5.js\",\r\n" + 
				"													\"path6\" : \"script6.js\"\r\n" + 
				"												}\r\n" + 
				"						  },\r\n" + 
				"						  {\r\n" + 
				"			\r\n" + 
				"							\"contributionName\" : \"Contribution2\",\r\n" + 
				"							\r\n" + 
				"							\"contributionHtmlName\" : \"example2.html\",\r\n" + 
				"							\r\n" + 
				"							\"contributedScripts\" : [\"script4.js\", \"script5.js\",\"script6.js\"],\r\n" + 
				"							\r\n" + 
				"							\"proxiedUris\" : [\"uri3\",\"uri4\"],\r\n" + 
				"							\r\n" + 
				"							\"pathsWithScripts\": {	\"path4\" : \"script4.js\",\r\n" + 
				"													\"path5\" : \"script5.js\",\r\n" + 
				"													\"path6\" : \"script6.js\"\r\n" + 
				"												}\r\n" + 
				"						  }\r\n" + 
				"	\r\n" + 
				"						]\r\n" + 
				"}");
		List<EquoContribution> contributions = configService.defineContributions(configJson.getAsJsonObject(), bundle);
		Assertions.assertThat(contributions).hasSize(2);
		Assertions.assertThat(contributions.get(0).getContributionName()).isEqualTo("contribution1");
		Assertions.assertThat(contributions.get(0).getContributedResourceName()).isEqualTo("example.html");
		Assertions.assertThat(contributions.get(0).getContributedScripts()).hasSize(3);
		Assertions.assertThat(contributions.get(0).getProxiedUris()).hasSize(2);
		Assertions.assertThat(contributions.get(0).getPathsToScripts().keySet()).hasSize(3);
		
		Assertions.assertThat(contributions.get(1).getContributionName()).isEqualTo("contribution2");
		Assertions.assertThat(contributions.get(1).getContributedResourceName()).isEqualTo("example2.html");
		Assertions.assertThat(contributions.get(1).getContributedScripts()).hasSize(3);
		Assertions.assertThat(contributions.get(1).getProxiedUris()).hasSize(2);
		Assertions.assertThat(contributions.get(1).getPathsToScripts().keySet()).hasSize(3);
		
	}

}
