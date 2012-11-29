package com.paypal.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.paypal.exception.ClientActionRequiredException;
import com.paypal.exception.SSLConfigurationException;
import com.paypal.exception.HttpErrorException;
import com.paypal.exception.InvalidCredentialException;
import com.paypal.exception.InvalidResponseDataException;
import com.paypal.exception.MissingCredentialException;
import com.paypal.sdk.exceptions.OAuthException;

/**
 * <code>BaseService</code> acts as base class for any concrete API service. The
 * Service class generated may extend this class to make API calls through HTTP
 */
public abstract class BaseService {

	/**
	 * Access Token used in third party authorization
	 */
	private String accessToken = null;

	/**
	 * Token secret used in third party authorization
	 */
	private String tokenSecret = null;

	/**
	 * Last request processed
	 */
	private String lastRequest = null;

	/**
	 * Last response received
	 */
	private String lastResponse = null;

	/**
	 * Gets the Access Token
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * Sets the Access Token 
	 * @deprecated
	 * @param accessToken
	 *            the accessToken to set
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * Gets the Token Secret
	 * @return the tokenSecret
	 */
	public String getTokenSecret() {
		return tokenSecret;
	}

	/**
	 * Sets the Token Secret
	 * @deprecated
	 * @param tokenSecret
	 *            the tokenSecret to set
	 */
	public void setTokenSecret(String tokenSecret) {
		this.tokenSecret = tokenSecret;
	}

	/**
	 * @return the lastRequest
	 */
	public String getLastRequest() {
		return lastRequest;
	}

	/**
	 * @param lastRequest
	 *            the lastRequest to set
	 */
	public void setLastRequest(String lastRequest) {
		this.lastRequest = lastRequest;
	}

	/**
	 * @return the lastResponse
	 */
	public String getLastResponse() {
		return lastResponse;
	}

	/**
	 * @param lastResponse
	 *            the lastResponse to set
	 */
	public void setLastResponse(String lastResponse) {
		this.lastResponse = lastResponse;
	}

	

	/**
	 * overloaded static method used to load the configuration file.
	 * 
	 * @param is
	 */
	public static void initConfig(InputStream is) throws IOException {
		try {
			ConfigManager.getInstance().load(is);
		} catch (IOException ioe) {
			LoggingManager.debug(BaseService.class, ioe.getMessage(), ioe);
			throw ioe;
		}
	}

	/**
	 * overloaded static method used to load the configuration file
	 * 
	 * @param file
	 */
	public static void initConfig(File file) throws IOException {
		try {
			if (!file.exists()) {
				throw new FileNotFoundException("File doesn't exist: "
						+ file.getAbsolutePath());
			}
			FileInputStream fis = new FileInputStream(file);
			initConfig(fis);
		} catch (IOException ioe) {
			LoggingManager.debug(BaseService.class, ioe.getMessage(), ioe);
			throw ioe;
		}
	}

	/**
	 * overloaded static method used to load the configuration file
	 * 
	 * @param filepath
	 */
	public static void initConfig(String filepath) throws IOException {
		try {
			File file = new File(filepath);
			initConfig(file);
		} catch (IOException ioe) {
			LoggingManager.debug(BaseService.class, ioe.getMessage(), ioe);
			throw ioe;
		}
	}

	/**
	 * Calls the APIService with the corresponding {@link APICallPreHandler}
	 * 
	 * @param apiCallPrehandler
	 *            {@link APICallPreHandler} instance
	 * @return Response as string from the API service
	 * @throws InvalidResponseDataException
	 * @throws HttpErrorException
	 * @throws ClientActionRequiredException
	 * @throws InvalidCredentialException
	 * @throws MissingCredentialException
	 * @throws OAuthException
	 * @throws SSLConfigurationException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	protected String call(APICallPreHandler apiCallPrehandler)
			throws InvalidResponseDataException, HttpErrorException,
			ClientActionRequiredException, InvalidCredentialException,
			MissingCredentialException, OAuthException,
			SSLConfigurationException, IOException, InterruptedException {
		if (!ConfigManager.getInstance().isPropertyLoaded()) {
			throw new FileNotFoundException("Property file not loaded");
		}
		APIService apiService = new APIService();
		lastRequest = apiCallPrehandler.getPayLoad();
		String response = apiService.makeRequestUsing(apiCallPrehandler);
		lastResponse = response;
		return response;
	}

}