package com.make.equo.server.provider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;

import org.littleshoot.proxy.MitmManager;
import org.littleshoot.proxy.mitm.FakeCertificateException;
import org.littleshoot.proxy.mitm.RootCertificateException;
import org.littleshoot.proxy.mitm.SubjectAlternativeNameHolder;
import org.mozilla.jss.CertDatabaseException;
import org.mozilla.jss.CertificateUsage;
import org.mozilla.jss.CryptoManager;
import org.mozilla.jss.InitializationValues;
import org.mozilla.jss.KeyDatabaseException;
import org.mozilla.jss.crypto.AlreadyInitializedException;
import org.mozilla.jss.crypto.InternalCertificate;
import org.mozilla.jss.crypto.X509Certificate;
import org.mozilla.jss.util.Password;

import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;

import org.mozilla.jss.NotInitializedException;

import io.netty.handler.codec.http.HttpRequest;

public class CustomHostNameMitmManager implements MitmManager{
	private CustomBouncyCastleSslEngineSource sslEngineSource;

	public CustomHostNameMitmManager() throws RootCertificateException {
		this(new CustomAuthority());
	}

	public CustomHostNameMitmManager(CustomAuthority authority)
			throws RootCertificateException {
		try {
			boolean trustAllServers = true;
			boolean sendCerts = true;
			sslEngineSource = new CustomBouncyCastleSslEngineSource(authority,
					trustAllServers, sendCerts);
		} catch (final Exception e) {
			throw new RootCertificateException(
					"Errors during assembling root CA.", e);
		}
	}

	public SSLEngine serverSslEngine(String peerHost, int peerPort) {
		return sslEngineSource.newSslEngine(peerHost, peerPort);
	}

	@Override
	public SSLEngine serverSslEngine() {
		return sslEngineSource.newSslEngine();
	}

	public SSLEngine clientSslEngineFor(HttpRequest httpRequest, SSLSession serverSslSession) {
		String serverHostAndPort = httpRequest.getUri();
		try {
			String serverName = serverHostAndPort.split(":")[0];
			SubjectAlternativeNameHolder san = new SubjectAlternativeNameHolder();
			san.addDomainName(serverName);
			return sslEngineSource.createCertForHost(serverName, san);
		} catch (Exception e) {
			throw new FakeCertificateException(
					"Creation dynamic certificate failed for "
							+ serverHostAndPort, e);
		}
	}
	
	private void importCert(Certificate cert, String name)throws Exception {
		byte[] certByte = null;
		try {
			certByte = cert.getEncoded();
			
			CryptoManager cryptoManager= null;
			try {
				cryptoManager = CryptoManager.getInstance();
				char[] passchar1 = {};
				Password pass1 = new Password(passchar1.clone());
				
				try {
					cryptoManager.getTokenByName("Internal Key Storage Token").initPassword(pass1, pass1);
				} catch (Exception e) {}
				
				X509Certificate ret = cryptoManager.importDERCert(certByte, CertificateUsage.SSLCA, true, name);
				InternalCertificate intern = (InternalCertificate)ret;
				intern.setSSLTrust(
					InternalCertificate.TRUSTED_CA |
					InternalCertificate.TRUSTED_CLIENT_CA |
					InternalCertificate.VALID_CA);
				intern.setEmailTrust(
					InternalCertificate.TRUSTED_CA |
					InternalCertificate.TRUSTED_CLIENT_CA |
					InternalCertificate.VALID_CA);
				intern.setObjectSigningTrust(
					InternalCertificate.TRUSTED_CA |
					InternalCertificate.TRUSTED_CLIENT_CA |
					InternalCertificate.VALID_CA);
				
			} catch (NotInitializedException e) {
				e.printStackTrace();
			}
		} catch (CertificateEncodingException e1) {}
	}
	
	public void inicializeCryptoManager(String fipsmode, String dbdir, String name) {
		if(sslEngineSource.getCert() == null)
			return;
		
		new File(dbdir).mkdirs();
		InitializationValues vals = new InitializationValues(dbdir, "", "", "");

		if (fipsmode.equalsIgnoreCase("enable")) {
			vals.fipsMode = InitializationValues.FIPSMode.ENABLED;
		} else if (fipsmode.equalsIgnoreCase("disable")){
			vals.fipsMode = InitializationValues.FIPSMode.DISABLED;
		} else {
			vals.fipsMode = InitializationValues.FIPSMode.UNCHANGED;
		}

		try {
			CryptoManager.initialize(vals);
			try {
				importCert(sslEngineSource.getCert(), name);
			} catch (Exception e) {
				e.printStackTrace();
			}
			java.security.Security.removeProvider("Mozilla-JSS");
		} catch (KeyDatabaseException e) {
			e.printStackTrace();
		} catch (CertDatabaseException e) {
			e.printStackTrace();
		} catch (AlreadyInitializedException e) {
			e.printStackTrace();
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
	}
	
	public void importCertWindows() {
		if(sslEngineSource.getCert() == null)
			return;
		String certPath = Paths.get(System.getProperty("user.home"),".equo","littleproxy-mitm.pem").toString();
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.command("powershell.exe", "/c", "Import-Certificate -FilePath "+certPath+" -CertStoreLocation Cert:\\CurrentUser\\Root");

		try {
			processBuilder.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
