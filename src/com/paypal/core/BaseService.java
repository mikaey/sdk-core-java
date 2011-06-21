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

/**
 * wrapper class for APIService.
 */
public class BaseService {

	private String serviceName;
	private String version;

	public BaseService(String serviceName, String version) {
		this.serviceName = serviceName;
		this.version = version;
	}

	public BaseService(String serviceName) {
		this.serviceName = serviceName;
	}

	/**
	 * overloaded static method used to load the configuration file.
	 * @param is
	 */
	public static void initConfig(InputStream is)throws IOException {
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
	public static void initConfig(File file) throws FileNotFoundException,IOException {
		try {
			if (!file.exists()) {
				throw new FileNotFoundException("File doesn't exist: "
						+ file.getAbsolutePath());
			}
			FileInputStream fis=new FileInputStream(file);
			initConfig(fis);
		} catch (FileNotFoundException fe) {
			LoggingManager.debug(BaseService.class, fe.getMessage(), fe);
			throw fe;
		} catch (IOException ioe) {
			LoggingManager.debug(BaseService.class, ioe.getMessage(), ioe);
			throw ioe;
		}
	}
	
	/**
	 * overloaded static method used to load the configuration file
	 * @param filepath
	 */
	public static void initConfig(String filepath)throws IOException,FileNotFoundException {
		try {
			File file = new File(filepath);
			initConfig(file);
		} catch (FileNotFoundException fe) {
			LoggingManager.debug(BaseService.class, fe.getMessage(), fe);
			throw fe;
		} catch (IOException ioe) {
			LoggingManager.debug(BaseService.class, ioe.getMessage(), ioe);
			throw ioe;
		}
	}
	/**
	 * Wrapper call for APIservice.makeRequest(), used by InvoiceService class.
	 * 
	 * @param method
	 *            (API method)
	 * @param payload
	 *            (request parameters)
	 * @param apiUsername
	 *            (PayPal account)
	 * @return String response
	 * @throws HttpErrorException
	 * @throws InterruptedException
	 * @throws InvalidResponseDataException
	 * @throws ClientActionRequiredException
	 * @throws MissingCredentialException
	 * @throws SSLConfigurationException
	 * @throws InvalidCredentialException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public String call(String method, String payload, String apiUsername)
			throws HttpErrorException, InterruptedException,
			InvalidResponseDataException, ClientActionRequiredException,
			MissingCredentialException, SSLConfigurationException,
			InvalidCredentialException, FileNotFoundException, IOException {
		if (!ConfigManager.getInstance().isPropertyLoaded()) {
			throw new FileNotFoundException("Property file not loaded");
		}
		APIService apiService = new APIService(serviceName);
		return apiService.makeRequest(method, payload, apiUsername);
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getVersion() {
		return version;
	}

}
