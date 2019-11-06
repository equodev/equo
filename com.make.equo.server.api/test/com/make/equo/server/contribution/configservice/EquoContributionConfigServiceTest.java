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
	public void testDefineContributionsBasicCase() {
		EquoContributionConfigService configService = new EquoContributionConfigService();
		Bundle bundle = null;
		JsonParser parser = new JsonParser();
		JsonElement configJson = parser.parse("{\r\n" + 
				"	\"contributions\" : 	[\r\n" + 
				"						  {\r\n" + 
				"			\r\n" + 
				"							\"contributionName\" : \"contribution\",\r\n" + 
				"							\r\n" + 
				"							\"contributionHtmlName\" : \"example.html\",\r\n" + 
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
		Assertions.assertThat(contributions).hasSize(1);
		Assertions.assertThat(contributions.get(0).getContributionName()).isEqualTo("contribution");
		Assertions.assertThat(contributions.get(0).getContributedResourceName()).isEqualTo("example.html");
		Assertions.assertThat(contributions.get(0).getContributedScripts()).hasSize(3);
		Assertions.assertThat(contributions.get(0).getProxiedUris()).hasSize(2);
		Assertions.assertThat(contributions.get(0).getPathsToScripts().keySet()).hasSize(3);
	}
	
	@Test
	public void testDefineContributionsCompoundCase() {
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
	
	
	@Test
	public void testDefineContributionsCompoundCaseMissingFields() {
		EquoContributionConfigService configService = new EquoContributionConfigService();
		Bundle bundle = null;
		JsonParser parser = new JsonParser();
		JsonElement configJson = parser.parse("{\r\n" + 
				"	\"contributions\" : 	[\r\n" + 
				"							{\r\n" + 
				"			\r\n" + 
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
				"							\"contributedScripts\" : [\"script4.js\", \"script5.js\",\"script6.js\"],\r\n" + 
				"							\r\n" + 
				"							\"proxiedUris\" : [\"uri3\",\"uri4\"],\r\n" + 
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
				"							\"proxiedUris\" : [\"uri3\",\"uri4\"],\r\n" + 
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
				"							\"proxiedUris\" : [\"uri3\",\"uri4\"]\r\n" + 
				"						  }\r\n" + 
				"	\r\n" + 
				"						]\r\n" + 
				"}");
		List<EquoContribution> contributions = configService.defineContributions(configJson.getAsJsonObject(), bundle);
		
		Assertions.assertThat(contributions).hasSize(5);
		
		//checkea el valor por defecto que el builder le asigna a las contribuciones a las cuales no se les asigna nombre.
		Assertions.assertThat(contributions.get(0).getContributionName()).isEqualTo("equocontribution1");
		
		//checkea el valor por defecto que el builder le asigna a las contribuciones a las cuales no se les asigna html.
		Assertions.assertThat(contributions.get(1).getContributedResourceName()).isEqualTo("");
		
		//checkea que no se hayan definido scripts
		Assertions.assertThat(contributions.get(2).getContributedScripts()).hasSize(0);
		
		//checkea que no se hayan definido proxiedUris
		Assertions.assertThat(contributions.get(3).getProxiedUris()).hasSize(0);
		
		//checkea que no se hayan definido paths con scripts
		Assertions.assertThat(contributions.get(4).getPathsToScripts().keySet()).hasSize(0);
		
	}
	
	@Test(expected = RuntimeException.class)
	public void testDefineContributionsExceptionCase() {
		EquoContributionConfigService configService = new EquoContributionConfigService();
		Bundle bundle = null;
		JsonParser parser = new JsonParser();
		JsonElement configJson = parser.parse("{\r\n" + 
				"	\"contributions\" : 	[\r\n" + 
				"						  {\r\n" + 
				"						  }\r\n" + 
				"	\r\n" + 
				"						]\r\n" + 
				"}");
		List<EquoContribution> contributions = configService.defineContributions(configJson.getAsJsonObject(), bundle);
		
	}
	
	@Test(expected = RuntimeException.class)
	public void testDefineContributionsExceptionCase2() {
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
				"						  }\r\n" + 
				"	\r\n" + 
				"						]\r\n" + 
				"}");
		List<EquoContribution> contributions = configService.defineContributions(configJson.getAsJsonObject(), bundle);
		
	}

}
