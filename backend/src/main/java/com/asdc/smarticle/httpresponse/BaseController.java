package com.asdc.smarticle.httpresponse;

import org.springframework.http.HttpStatus;

/**
* Methods to send generalize Http response.  
* @author  Vivekkumar Patel
* @version 1.0
* @since   2022-02-19
*/
public class BaseController {

	/**
	 * prepare success response .
	 *
	 * @param data request header
	 * @return Response object.
	 */
	protected <D> ResponseVO<D> prepareSuccessResponse(D data) {

		ResponseVO<D> responseVO = new ResponseVO<D>();
		responseVO.setData(data);
		responseVO.setStatusCode(HttpStatus.OK.value());
		responseVO.setMessage(HttpStatus.OK.name());
		responseVO.setStatus(true);
		return responseVO;
	}

	/**
	 * prepare error response .
	 *
	 * @param data request header
	 * @return Response object.
	 */
	protected <D> ResponseVO<D> prepareErrorResponse(D data) {

		ResponseVO<D> responseVO = new ResponseVO<D>();
		responseVO.setData(data);
		responseVO.setStatusCode(HttpStatus.BAD_REQUEST.value());
		responseVO.setMessage(HttpStatus.BAD_REQUEST.name());
		responseVO.setStatus(false);
		return responseVO;
	}

	/**
	 * Success response .
	 *
	 * @param statusCode
	 * @param message
	 * @param status
	 * @return Response object.
	 */
	protected <D> ResponseVO<D> success(int statusCode, String message, boolean status) {

		ResponseVO<D> responseVO = new ResponseVO<D>();
		responseVO.setStatusCode(statusCode);
		responseVO.setStatus(status);
		responseVO.setMessage(message);
		return responseVO;
	}

	/**
	 * Error response .
	 *
	 * @param statusCode
	 * @param message
	 * @param status
	 * @return Response object.
	 */
	protected <D> ResponseVO<D> error(int statusCode, String message, boolean status) {

		ResponseVO<D> responseVO = new ResponseVO<D>();
		responseVO.setStatusCode(statusCode);
		responseVO.setStatus(status);
		responseVO.setMessage(message);
		return responseVO;
	}

}
