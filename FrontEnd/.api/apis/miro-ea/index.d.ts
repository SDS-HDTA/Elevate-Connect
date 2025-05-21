import type * as types from './types';
import type { ConfigOptions, FetchResponse } from 'api/dist/core';
import Oas from 'oas';
import APICore from 'api/dist/core';
declare class SDK {
    spec: Oas;
    core: APICore;
    constructor();
    /**
     * Optionally configure various options that the SDK allows.
     *
     * @param config Object of supported SDK options and toggles.
     * @param config.timeout Override the default `fetch` request timeout of 30 seconds. This number
     * should be represented in milliseconds.
     */
    config(config: ConfigOptions): void;
    /**
     * If the API you're using requires authentication you can supply the required credentials
     * through this method and the library will magically determine how they should be used
     * within your API request.
     *
     * With the exception of OpenID and MutualTLS, it supports all forms of authentication
     * supported by the OpenAPI specification.
     *
     * @example <caption>HTTP Basic auth</caption>
     * sdk.auth('username', 'password');
     *
     * @example <caption>Bearer tokens (HTTP or OAuth 2)</caption>
     * sdk.auth('myBearerToken');
     *
     * @example <caption>API Keys</caption>
     * sdk.auth('myApiKey');
     *
     * @see {@link https://spec.openapis.org/oas/v3.0.3#fixed-fields-22}
     * @see {@link https://spec.openapis.org/oas/v3.1.0#fixed-fields-22}
     * @param values Your auth credentials for the API; can specify up to two strings or numbers.
     */
    auth(...values: string[] | number[]): this;
    /**
     * If the API you're using offers alternate server URLs, and server variables, you can tell
     * the SDK which one to use with this method. To use it you can supply either one of the
     * server URLs that are contained within the OpenAPI definition (along with any server
     * variables), or you can pass it a fully qualified URL to use (that may or may not exist
     * within the OpenAPI definition).
     *
     * @example <caption>Server URL with server variables</caption>
     * sdk.server('https://{region}.api.example.com/{basePath}', {
     *   name: 'eu',
     *   basePath: 'v14',
     * });
     *
     * @example <caption>Fully qualified server URL</caption>
     * sdk.server('https://eu.api.example.com/v14');
     *
     * @param url Server URL
     * @param variables An object of variables to replace into the server URL.
     */
    server(url: string, variables?: {}): void;
    /**
     * Creates a board with the specified name and sharing policies.<br/><h4>Note</h4> You can
     * only create up to 3 team boards with the free plan.<br/><h3>Required scope</h3> <a
     * target="blank" href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a
     * target="blank" href="/reference/rate-limiting#rate-limit-tiers">Level 3</a><br/>
     *
     * @summary Create board
     * @throws FetchError<400, types.CreateBoardResponse400> Malformed request
     * @throws FetchError<404, types.CreateBoardResponse404> Invalid access
     * @throws FetchError<409, types.CreateBoardResponse409> Conflict
     * @throws FetchError<429, types.CreateBoardResponse429> Too many requests
     */
    createBoard(body?: types.CreateBoardBodyParam): Promise<FetchResponse<201, types.CreateBoardResponse201>>;
    /**
     * Retrieves a list of boards accessible to the user associated with the provided access
     * token. This endpoint supports filtering and sorting through URL query parameters.
     * Customize the response by specifying `team_id`, `project_id`, or other query parameters.
     * Filtering by `team_id` or `project_id` (or both) returns results instantly. For other
     * filters, allow a few seconds for indexing of newly created boards.
     *
     * If you're an Enterprise customer with Company Admin permissions:
     *
     * - Enable **Content Admin** permissions to retrieve all boards, including private boards
     * (those not explicitly shared with you). For details, see the [Content Admin Permissions
     * for Company
     * Admins](https://help.miro.com/hc/en-us/articles/360012777280-Content-Admin-permissions-for-Company-Admins).
     *
     * - Note that **Private board contents remain inaccessible**. The API allows you to verify
     * their existence but prevents viewing their contents to uphold security best practices.
     * Unauthorized access attempts will return an error.
     * <h3>Required scope</h3> <a target="blank" href="/reference/scopes">boards:read</a>
     * <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 1</a><br/>
     *
     * @summary Get boards
     * @throws FetchError<400, types.GetBoardsResponse400> Malformed request
     * @throws FetchError<404, types.GetBoardsResponse404> Invalid access
     * @throws FetchError<429, types.GetBoardsResponse429> Too many requests
     */
    getBoards(metadata?: types.GetBoardsMetadataParam): Promise<FetchResponse<200, types.GetBoardsResponse200>>;
    /**
     * Creates a copy of an existing board. You can also update the name, description, sharing
     * policy, and permissions policy for the new board in the request body.<br/><h3>Required
     * scope</h3> <a target="blank" href="/reference/scopes">boards:write</a> <br/><h3>Rate
     * limiting</h3> <a target="blank" href="/reference/rate-limiting#rate-limit-tiers">Level
     * 4</a><br/>
     *
     * @summary Copy board
     * @throws FetchError<400, types.CopyBoardResponse400> Malformed request
     * @throws FetchError<404, types.CopyBoardResponse404> Invalid access
     * @throws FetchError<409, types.CopyBoardResponse409> Conflict
     * @throws FetchError<429, types.CopyBoardResponse429> Too many requests
     */
    copyBoard(body: types.CopyBoardBodyParam, metadata: types.CopyBoardMetadataParam): Promise<FetchResponse<201, types.CopyBoardResponse201>>;
    copyBoard(metadata: types.CopyBoardMetadataParam): Promise<FetchResponse<201, types.CopyBoardResponse201>>;
    /**
     * Retrieves information about a board.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:read</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 1</a><br/>
     *
     * @summary Get specific board
     * @throws FetchError<400, types.GetSpecificBoardResponse400> Malformed request
     * @throws FetchError<404, types.GetSpecificBoardResponse404> Invalid access
     * @throws FetchError<429, types.GetSpecificBoardResponse429> Too many requests
     */
    getSpecificBoard(metadata: types.GetSpecificBoardMetadataParam): Promise<FetchResponse<200, types.GetSpecificBoardResponse200>>;
    /**
     * Updates a specific board.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Update board
     * @throws FetchError<400, types.UpdateBoardResponse400> Malformed request
     * @throws FetchError<404, types.UpdateBoardResponse404> Invalid access
     * @throws FetchError<409, types.UpdateBoardResponse409> Conflict
     * @throws FetchError<429, types.UpdateBoardResponse429> Too many requests
     */
    updateBoard(body: types.UpdateBoardBodyParam, metadata: types.UpdateBoardMetadataParam): Promise<FetchResponse<200, types.UpdateBoardResponse200>>;
    /**
     * Deletes a board. Deleted boards go to Trash (on paid plans) and can be restored via UI
     * within 90 days after deletion.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 3</a><br/>
     *
     * @summary Delete board
     * @throws FetchError<400, types.DeleteBoardResponse400> Malformed request
     * @throws FetchError<404, types.DeleteBoardResponse404> Invalid access
     * @throws FetchError<409, types.DeleteBoardResponse409> Conflict
     * @throws FetchError<429, types.DeleteBoardResponse429> Too many requests
     */
    deleteBoard(metadata: types.DeleteBoardMetadataParam): Promise<FetchResponse<204, types.DeleteBoardResponse204>>;
    /**
     * Adds an app card item to a board.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Create app card item
     * @throws FetchError<400, types.CreateAppCardItemResponse400> Malformed request
     * @throws FetchError<404, types.CreateAppCardItemResponse404> Invalid access
     * @throws FetchError<429, types.CreateAppCardItemResponse429> Too many requests
     */
    createAppCardItem(body: types.CreateAppCardItemBodyParam, metadata: types.CreateAppCardItemMetadataParam): Promise<FetchResponse<201, types.CreateAppCardItemResponse201>>;
    /**
     * Retrieves information for a specific app card item on a board.<br/><h3>Required
     * scope</h3> <a target="blank" href="/reference/scopes">boards:read</a> <br/><h3>Rate
     * limiting</h3> <a target="blank" href="/reference/rate-limiting#rate-limit-tiers">Level
     * 1</a><br/>
     *
     * @summary Get app card item
     * @throws FetchError<400, types.GetAppCardItemResponse400> Malformed request
     * @throws FetchError<404, types.GetAppCardItemResponse404> Invalid access
     * @throws FetchError<429, types.GetAppCardItemResponse429> Too many requests
     */
    getAppCardItem(metadata: types.GetAppCardItemMetadataParam): Promise<FetchResponse<200, types.GetAppCardItemResponse200>>;
    /**
     * Updates an app card item on a board based on the data and style properties provided in
     * the request body.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Update app card item
     * @throws FetchError<400, types.UpdateAppCardItemResponse400> Malformed request
     * @throws FetchError<404, types.UpdateAppCardItemResponse404> Invalid access
     * @throws FetchError<409, types.UpdateAppCardItemResponse409> Conflict
     * @throws FetchError<429, types.UpdateAppCardItemResponse429> Too many requests
     */
    updateAppCardItem(body: types.UpdateAppCardItemBodyParam, metadata: types.UpdateAppCardItemMetadataParam): Promise<FetchResponse<200, types.UpdateAppCardItemResponse200>>;
    /**
     * Deletes an app card item from a board.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 3</a><br/>
     *
     * @summary Delete app card item
     * @throws FetchError<400, types.DeleteAppCardItemResponse400> Malformed request
     * @throws FetchError<404, types.DeleteAppCardItemResponse404> Invalid access
     * @throws FetchError<429, types.DeleteAppCardItemResponse429> Too many requests
     */
    deleteAppCardItem(metadata: types.DeleteAppCardItemMetadataParam): Promise<FetchResponse<204, types.DeleteAppCardItemResponse204>>;
    /**
     * Adds a card item to a board<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Create card item
     * @throws FetchError<400, types.CreateCardItemResponse400> Malformed request
     * @throws FetchError<404, types.CreateCardItemResponse404> Invalid access
     * @throws FetchError<429, types.CreateCardItemResponse429> Too many requests
     */
    createCardItem(body: types.CreateCardItemBodyParam, metadata: types.CreateCardItemMetadataParam): Promise<FetchResponse<201, types.CreateCardItemResponse201>>;
    /**
     * Retrieves information for a specific card item on a board<br/><h3>Required scope</h3> <a
     * target="blank" href="/reference/scopes">boards:read</a> <br/><h3>Rate limiting</h3> <a
     * target="blank" href="/reference/rate-limiting#rate-limit-tiers">Level 1</a><br/>
     *
     * @summary Get card item
     * @throws FetchError<400, types.GetCardItemResponse400> Malformed request
     * @throws FetchError<404, types.GetCardItemResponse404> Invalid access
     * @throws FetchError<429, types.GetCardItemResponse429> Too many requests
     */
    getCardItem(metadata: types.GetCardItemMetadataParam): Promise<FetchResponse<200, types.GetCardItemResponse200>>;
    /**
     * Updates a card item on a board based on the data and style properties provided in the
     * request body.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Update card item
     * @throws FetchError<400, types.UpdateCardItemResponse400> Malformed request
     * @throws FetchError<404, types.UpdateCardItemResponse404> Invalid access
     * @throws FetchError<409, types.UpdateCardItemResponse409> Conflict
     * @throws FetchError<429, types.UpdateCardItemResponse429> Too many requests
     */
    updateCardItem(body: types.UpdateCardItemBodyParam, metadata: types.UpdateCardItemMetadataParam): Promise<FetchResponse<200, types.UpdateCardItemResponse200>>;
    /**
     * Deletes a card item from the board.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 3</a><br/>
     *
     * @summary Delete card item
     * @throws FetchError<400, types.DeleteCardItemResponse400> Malformed request
     * @throws FetchError<404, types.DeleteCardItemResponse404> Invalid access
     * @throws FetchError<429, types.DeleteCardItemResponse429> Too many requests
     */
    deleteCardItem(metadata: types.DeleteCardItemMetadataParam): Promise<FetchResponse<204, types.DeleteCardItemResponse204>>;
    /**
     * Adds a connector to a board.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Create connector
     * @throws FetchError<400, types.CreateConnectorResponse400> Malformed request
     * @throws FetchError<404, types.CreateConnectorResponse404> Invalid access
     * @throws FetchError<429, types.CreateConnectorResponse429> Too many requests
     */
    createConnector(body: types.CreateConnectorBodyParam, metadata: types.CreateConnectorMetadataParam): Promise<FetchResponse<200, types.CreateConnectorResponse200>>;
    /**
     * Retrieves a list of connectors for a specific board.
     *
     * This method returns results using a cursor-based approach. A cursor-paginated method
     * returns a portion of the total set of results based on the limit specified and a cursor
     * that points to the next portion of the results. To retrieve the next portion of the
     * collection, on your next call to the same method, set the `cursor` parameter equal to
     * the `cursor` value you received in the response of the previous request. For example, if
     * you set the `limit` query parameter to `10` and the board contains 20 objects, the first
     * call will return information about the first 10 objects in the response along with a
     * cursor parameter and value. In this example, let's say the cursor parameter value
     * returned in the response is `foo`. If you want to retrieve the next set of objects, on
     * your next call to the same method, set the cursor parameter value to
     * `foo`.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:read</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Get connectors
     * @throws FetchError<400, types.GetConnectorsResponse400> Malformed request
     * @throws FetchError<404, types.GetConnectorsResponse404> Invalid access
     * @throws FetchError<429, types.GetConnectorsResponse429> Too many requests
     */
    getConnectors(metadata: types.GetConnectorsMetadataParam): Promise<FetchResponse<200, types.GetConnectorsResponse200>>;
    /**
     * Retrieves information for a specific connector on a board.<br/><h3>Required scope</h3>
     * <a target="blank" href="/reference/scopes">boards:read</a> <br/><h3>Rate limiting</h3>
     * <a target="blank" href="/reference/rate-limiting#rate-limit-tiers">Level 1</a><br/>
     *
     * @summary Get specific connector
     * @throws FetchError<400, types.GetConnectorResponse400> Malformed request
     * @throws FetchError<404, types.GetConnectorResponse404> Invalid access
     * @throws FetchError<429, types.GetConnectorResponse429> Too many requests
     */
    getConnector(metadata: types.GetConnectorMetadataParam): Promise<FetchResponse<200, types.GetConnectorResponse200>>;
    /**
     * Updates a connector on a board based on the data and style properties provided in the
     * request body.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Update connector
     * @throws FetchError<400, types.UpdateConnectorResponse400> Malformed request
     * @throws FetchError<404, types.UpdateConnectorResponse404> Invalid access
     * @throws FetchError<409, types.UpdateConnectorResponse409> Conflict
     * @throws FetchError<429, types.UpdateConnectorResponse429> Too many requests
     */
    updateConnector(body: types.UpdateConnectorBodyParam, metadata: types.UpdateConnectorMetadataParam): Promise<FetchResponse<200, types.UpdateConnectorResponse200>>;
    /**
     * Deletes the specified connector from the board.<br/><h3>Required scope</h3> <a
     * target="blank" href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a
     * target="blank" href="/reference/rate-limiting#rate-limit-tiers">Level 3</a><br/>
     *
     * @summary Delete connector
     * @throws FetchError<400, types.DeleteConnectorResponse400> Malformed request
     * @throws FetchError<404, types.DeleteConnectorResponse404> Invalid access
     * @throws FetchError<429, types.DeleteConnectorResponse429> Too many requests
     */
    deleteConnector(metadata: types.DeleteConnectorMetadataParam): Promise<FetchResponse<204, types.DeleteConnectorResponse204>>;
    /**
     * Adds a document item to a board by specifying the URL where the document is
     * hosted.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Create document item using URL
     * @throws FetchError<400, types.CreateDocumentItemUsingUrlResponse400> Malformed request
     * @throws FetchError<404, types.CreateDocumentItemUsingUrlResponse404> Invalid access
     * @throws FetchError<429, types.CreateDocumentItemUsingUrlResponse429> Too many requests
     */
    createDocumentItemUsingUrl(body: types.CreateDocumentItemUsingUrlBodyParam, metadata: types.CreateDocumentItemUsingUrlMetadataParam): Promise<FetchResponse<201, types.CreateDocumentItemUsingUrlResponse201>>;
    /**
     * Retrieves information for a specific document item on a board<br/><h3>Required
     * scope</h3> <a target="blank" href="/reference/scopes">boards:read</a> <br/><h3>Rate
     * limiting</h3> <a target="blank" href="/reference/rate-limiting#rate-limit-tiers">Level
     * 1</a><br/>
     *
     * @summary Get document item
     * @throws FetchError<400, types.GetDocumentItemResponse400> Malformed request
     * @throws FetchError<404, types.GetDocumentItemResponse404> Invalid access
     * @throws FetchError<429, types.GetDocumentItemResponse429> Too many requests
     */
    getDocumentItem(metadata: types.GetDocumentItemMetadataParam): Promise<FetchResponse<200, types.GetDocumentItemResponse200>>;
    /**
     * Updates a document item on a board<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Update document item using URL
     * @throws FetchError<400, types.UpdateDocumentItemUsingUrlResponse400> Malformed request
     * @throws FetchError<404, types.UpdateDocumentItemUsingUrlResponse404> Invalid access
     * @throws FetchError<409, types.UpdateDocumentItemUsingUrlResponse409> Conflict
     * @throws FetchError<429, types.UpdateDocumentItemUsingUrlResponse429> Too many requests
     */
    updateDocumentItemUsingUrl(body: types.UpdateDocumentItemUsingUrlBodyParam, metadata: types.UpdateDocumentItemUsingUrlMetadataParam): Promise<FetchResponse<200, types.UpdateDocumentItemUsingUrlResponse200>>;
    /**
     * Deletes a document item from the board.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 3</a><br/>
     *
     * @summary Delete document item
     * @throws FetchError<400, types.DeleteDocumentItemResponse400> Malformed request
     * @throws FetchError<404, types.DeleteDocumentItemResponse404> Invalid access
     * @throws FetchError<429, types.DeleteDocumentItemResponse429> Too many requests
     */
    deleteDocumentItem(metadata: types.DeleteDocumentItemMetadataParam): Promise<FetchResponse<204, types.DeleteDocumentItemResponse204>>;
    /**
     * Adds an embed item containing external content to a board.<br/><h3>Required scope</h3>
     * <a target="blank" href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3>
     * <a target="blank" href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Create embed item
     * @throws FetchError<400, types.CreateEmbedItemResponse400> Malformed request
     * @throws FetchError<404, types.CreateEmbedItemResponse404> Invalid access
     * @throws FetchError<429, types.CreateEmbedItemResponse429> Too many requests
     */
    createEmbedItem(body: types.CreateEmbedItemBodyParam, metadata: types.CreateEmbedItemMetadataParam): Promise<FetchResponse<201, types.CreateEmbedItemResponse201>>;
    /**
     * Retrieves information for a specific embed item on a board.<br/><h3>Required scope</h3>
     * <a target="blank" href="/reference/scopes">boards:read</a> <br/><h3>Rate limiting</h3>
     * <a target="blank" href="/reference/rate-limiting#rate-limit-tiers">Level 1</a><br/>
     *
     * @summary Get embed item
     * @throws FetchError<400, types.GetEmbedItemResponse400> Malformed request
     * @throws FetchError<404, types.GetEmbedItemResponse404> Invalid access
     * @throws FetchError<429, types.GetEmbedItemResponse429> Too many requests
     */
    getEmbedItem(metadata: types.GetEmbedItemMetadataParam): Promise<FetchResponse<200, types.GetEmbedItemResponse200>>;
    /**
     * Updates an embed item on a board based on the data properties provided in the request
     * body.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Update embed item
     * @throws FetchError<400, types.UpdateEmbedItemResponse400> Malformed request
     * @throws FetchError<404, types.UpdateEmbedItemResponse404> Invalid access
     * @throws FetchError<409, types.UpdateEmbedItemResponse409> Conflict
     * @throws FetchError<429, types.UpdateEmbedItemResponse429> Too many requests
     */
    updateEmbedItem(body: types.UpdateEmbedItemBodyParam, metadata: types.UpdateEmbedItemMetadataParam): Promise<FetchResponse<200, types.UpdateEmbedItemResponse200>>;
    /**
     * Deletes an embed item from the board.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 3</a><br/>
     *
     * @summary Delete embed item
     * @throws FetchError<400, types.DeleteEmbedItemResponse400> Malformed request
     * @throws FetchError<404, types.DeleteEmbedItemResponse404> Invalid access
     * @throws FetchError<429, types.DeleteEmbedItemResponse429> Too many requests
     */
    deleteEmbedItem(metadata: types.DeleteEmbedItemMetadataParam): Promise<FetchResponse<204, types.DeleteEmbedItemResponse204>>;
    /**
     * Adds an image item to a board by specifying an image URL.<br/><h3>Required scope</h3> <a
     * target="blank" href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a
     * target="blank" href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Create image item using URL
     * @throws FetchError<400, types.CreateImageItemUsingUrlResponse400> Malformed request
     * @throws FetchError<404, types.CreateImageItemUsingUrlResponse404> Invalid access
     * @throws FetchError<429, types.CreateImageItemUsingUrlResponse429> Too many requests
     */
    createImageItemUsingUrl(body: types.CreateImageItemUsingUrlBodyParam, metadata: types.CreateImageItemUsingUrlMetadataParam): Promise<FetchResponse<201, types.CreateImageItemUsingUrlResponse201>>;
    /**
     * Retrieves information for a specific image item on a board.<br/><h3>Required scope</h3>
     * <a target="blank" href="/reference/scopes">boards:read</a> <br/><h3>Rate limiting</h3>
     * <a target="blank" href="/reference/rate-limiting#rate-limit-tiers">Level 1</a><br/>
     *
     * @summary Get image item
     * @throws FetchError<400, types.GetImageItemResponse400> Malformed request
     * @throws FetchError<404, types.GetImageItemResponse404> Invalid access
     * @throws FetchError<429, types.GetImageItemResponse429> Too many requests
     */
    getImageItem(metadata: types.GetImageItemMetadataParam): Promise<FetchResponse<200, types.GetImageItemResponse200>>;
    /**
     * Updates an image item on a board.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Update image item using URL
     * @throws FetchError<400, types.UpdateImageItemUsingUrlResponse400> Malformed request
     * @throws FetchError<404, types.UpdateImageItemUsingUrlResponse404> Invalid access
     * @throws FetchError<409, types.UpdateImageItemUsingUrlResponse409> Conflict
     * @throws FetchError<429, types.UpdateImageItemUsingUrlResponse429> Too many requests
     */
    updateImageItemUsingUrl(body: types.UpdateImageItemUsingUrlBodyParam, metadata: types.UpdateImageItemUsingUrlMetadataParam): Promise<FetchResponse<200, types.UpdateImageItemUsingUrlResponse200>>;
    /**
     * Deletes an image item from the board.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 3</a><br/>
     *
     * @summary Delete image item
     * @throws FetchError<400, types.DeleteImageItemResponse400> Malformed request
     * @throws FetchError<404, types.DeleteImageItemResponse404> Invalid access
     * @throws FetchError<429, types.DeleteImageItemResponse429> Too many requests
     */
    deleteImageItem(metadata: types.DeleteImageItemMetadataParam): Promise<FetchResponse<204, types.DeleteImageItemResponse204>>;
    /**
     * Retrieves a list of items for a specific board. You can retrieve all items on the board,
     * a list of child items inside a parent item, or a list of specific types of items by
     * specifying URL query parameter values.
     *
     * This method returns results using a cursor-based approach. A cursor-paginated method
     * returns a portion of the total set of results based on the limit specified and a cursor
     * that points to the next portion of the results. To retrieve the next portion of the
     * collection, on your next call to the same method, set the `cursor` parameter equal to
     * the `cursor` value you received in the response of the previous request. For example, if
     * you set the `limit` query parameter to `10` and the board contains 20 objects, the first
     * call will return information about the first 10 objects in the response along with a
     * cursor parameter and value. In this example, let's say the cursor parameter value
     * returned in the response is `foo`. If you want to retrieve the next set of objects, on
     * your next call to the same method, set the cursor parameter value to
     * `foo`.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:read</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Get items on board
     * @throws FetchError<400, types.GetItemsResponse400> Malformed request
     * @throws FetchError<404, types.GetItemsResponse404> Invalid access
     * @throws FetchError<429, types.GetItemsResponse429> Too many requests
     */
    getItems(metadata: types.GetItemsMetadataParam): Promise<FetchResponse<200, types.GetItemsResponse200>>;
    /**
     * Retrieves information for a specific item on a board.<br/><h3>Required scope</h3> <a
     * target="blank" href="/reference/scopes">boards:read</a> <br/><h3>Rate limiting</h3> <a
     * target="blank" href="/reference/rate-limiting#rate-limit-tiers">Level 1</a><br/>
     *
     * @summary Get specific item on board
     * @throws FetchError<400, types.GetSpecificItemResponse400> Malformed request
     * @throws FetchError<404, types.GetSpecificItemResponse404> Invalid access
     * @throws FetchError<429, types.GetSpecificItemResponse429> Too many requests
     */
    getSpecificItem(metadata: types.GetSpecificItemMetadataParam): Promise<FetchResponse<200, types.GetSpecificItemResponse200>>;
    /**
     * Updates the position or the parent of an item on a board.<br/><h3>Required scope</h3> <a
     * target="blank" href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a
     * target="blank" href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Update item position or parent
     * @throws FetchError<400, types.UpdateItemPositionOrParentResponse400> Malformed request
     * @throws FetchError<404, types.UpdateItemPositionOrParentResponse404> Invalid access
     * @throws FetchError<429, types.UpdateItemPositionOrParentResponse429> Too many requests
     */
    updateItemPositionOrParent(body: types.UpdateItemPositionOrParentBodyParam, metadata: types.UpdateItemPositionOrParentMetadataParam): Promise<FetchResponse<200, types.UpdateItemPositionOrParentResponse200>>;
    /**
     * Deletes an item from a board.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 3</a><br/>
     *
     * @summary Delete item
     * @throws FetchError<400, types.DeleteItemResponse400> Malformed request
     * @throws FetchError<404, types.DeleteItemResponse404> Invalid access
     * @throws FetchError<429, types.DeleteItemResponse429> Too many requests
     */
    deleteItem(metadata: types.DeleteItemMetadataParam): Promise<FetchResponse<204, types.DeleteItemResponse204>>;
    /**
     * Shares the board and Invites new members to collaborate on a board by sending an
     * invitation email. Depending on the board's Sharing policy, there might be various
     * scenarios where membership in the team is required in order to share the board with a
     * user.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 3</a><br/>
     *
     * @summary Share board
     * @throws FetchError<400, types.ShareBoardResponse400> Malformed request
     * @throws FetchError<404, types.ShareBoardResponse404> Invalid access
     * @throws FetchError<429, types.ShareBoardResponse429> Too many requests
     */
    shareBoard(body: types.ShareBoardBodyParam, metadata: types.ShareBoardMetadataParam): Promise<FetchResponse<201, types.ShareBoardResponse201>>;
    /**
     * Retrieves a pageable list of members for a board.<br/><h3>Required scope</h3> <a
     * target="blank" href="/reference/scopes">boards:read</a> <br/><h3>Rate limiting</h3> <a
     * target="blank" href="/reference/rate-limiting#rate-limit-tiers">Level 1</a><br/>
     *
     * @summary Get all board members
     * @throws FetchError<400, types.GetBoardMembersResponse400> Malformed request
     * @throws FetchError<404, types.GetBoardMembersResponse404> Invalid access
     * @throws FetchError<429, types.GetBoardMembersResponse429> Too many requests
     */
    getBoardMembers(metadata: types.GetBoardMembersMetadataParam): Promise<FetchResponse<200, types.GetBoardMembersResponse200>>;
    /**
     * Retrieves information for a board member.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:read</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 1</a><br/>
     *
     * @summary Get specific board member
     * @throws FetchError<400, types.GetSpecificBoardMemberResponse400> Malformed request
     * @throws FetchError<404, types.GetSpecificBoardMemberResponse404> Invalid access
     * @throws FetchError<429, types.GetSpecificBoardMemberResponse429> Too many requests
     */
    getSpecificBoardMember(metadata: types.GetSpecificBoardMemberMetadataParam): Promise<FetchResponse<200, types.GetSpecificBoardMemberResponse200>>;
    /**
     * Updates the role of a board member.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Update board member
     * @throws FetchError<400, types.UpdateBoardMemberResponse400> Malformed request
     * @throws FetchError<404, types.UpdateBoardMemberResponse404> Invalid access
     * @throws FetchError<429, types.UpdateBoardMemberResponse429> Too many requests
     */
    updateBoardMember(body: types.UpdateBoardMemberBodyParam, metadata: types.UpdateBoardMemberMetadataParam): Promise<FetchResponse<200, types.UpdateBoardMemberResponse200>>;
    /**
     * Removes a board member from a board.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Remove board member
     * @throws FetchError<400, types.RemoveBoardMemberResponse400> Malformed request
     * @throws FetchError<404, types.RemoveBoardMemberResponse404> Invalid access
     * @throws FetchError<429, types.RemoveBoardMemberResponse429> Too many requests
     */
    removeBoardMember(metadata: types.RemoveBoardMemberMetadataParam): Promise<FetchResponse<204, types.RemoveBoardMemberResponse204>>;
    /**
     * Adds a shape item to a board.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Create shape item
     * @throws FetchError<400, types.CreateShapeItemResponse400> Malformed request
     * @throws FetchError<404, types.CreateShapeItemResponse404> Invalid access
     * @throws FetchError<429, types.CreateShapeItemResponse429> Too many requests
     */
    createShapeItem(body: types.CreateShapeItemBodyParam, metadata: types.CreateShapeItemMetadataParam): Promise<FetchResponse<201, types.CreateShapeItemResponse201>>;
    /**
     * Retrieves information for a specific shape item on a board.<br/><h3>Required scope</h3>
     * <a target="blank" href="/reference/scopes">boards:read</a> <br/><h3>Rate limiting</h3>
     * <a target="blank" href="/reference/rate-limiting#rate-limit-tiers">Level 1</a><br/>
     *
     * @summary Get shape item
     * @throws FetchError<400, types.GetShapeItemResponse400> Malformed request
     * @throws FetchError<404, types.GetShapeItemResponse404> Invalid access
     * @throws FetchError<429, types.GetShapeItemResponse429> Too many requests
     */
    getShapeItem(metadata: types.GetShapeItemMetadataParam): Promise<FetchResponse<200, types.GetShapeItemResponse200>>;
    /**
     * Updates a shape item on a board based on the data and style properties provided in the
     * request body.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Update shape item
     * @throws FetchError<400, types.UpdateShapeItemResponse400> Malformed request
     * @throws FetchError<404, types.UpdateShapeItemResponse404> Invalid access
     * @throws FetchError<409, types.UpdateShapeItemResponse409> Conflict
     * @throws FetchError<429, types.UpdateShapeItemResponse429> Too many requests
     */
    updateShapeItem(body: types.UpdateShapeItemBodyParam, metadata: types.UpdateShapeItemMetadataParam): Promise<FetchResponse<200, types.UpdateShapeItemResponse200>>;
    /**
     * Deletes a shape item from the board.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 3</a><br/>
     *
     * @summary Delete shape item
     * @throws FetchError<400, types.DeleteShapeItemResponse400> Malformed request
     * @throws FetchError<404, types.DeleteShapeItemResponse404> Invalid access
     * @throws FetchError<429, types.DeleteShapeItemResponse429> Too many requests
     */
    deleteShapeItem(metadata: types.DeleteShapeItemMetadataParam): Promise<FetchResponse<204, types.DeleteShapeItemResponse204>>;
    /**
     * Adds a sticky note item to a board.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Create sticky note item
     * @throws FetchError<400, types.CreateStickyNoteItemResponse400> Malformed request
     * @throws FetchError<404, types.CreateStickyNoteItemResponse404> Invalid access
     * @throws FetchError<429, types.CreateStickyNoteItemResponse429> Too many requests
     */
    createStickyNoteItem(body: types.CreateStickyNoteItemBodyParam, metadata: types.CreateStickyNoteItemMetadataParam): Promise<FetchResponse<201, types.CreateStickyNoteItemResponse201>>;
    /**
     * Retrieves information for a specific sticky note item on a board.<br/><h3>Required
     * scope</h3> <a target="blank" href="/reference/scopes">boards:read</a> <br/><h3>Rate
     * limiting</h3> <a target="blank" href="/reference/rate-limiting#rate-limit-tiers">Level
     * 1</a><br/>
     *
     * @summary Get sticky note item
     * @throws FetchError<400, types.GetStickyNoteItemResponse400> Malformed request
     * @throws FetchError<404, types.GetStickyNoteItemResponse404> Invalid access
     * @throws FetchError<429, types.GetStickyNoteItemResponse429> Too many requests
     */
    getStickyNoteItem(metadata: types.GetStickyNoteItemMetadataParam): Promise<FetchResponse<200, types.GetStickyNoteItemResponse200>>;
    /**
     * Updates a sticky note item on a board based on the data and style properties provided in
     * the request body.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Update sticky note item
     * @throws FetchError<400, types.UpdateStickyNoteItemResponse400> Malformed request
     * @throws FetchError<404, types.UpdateStickyNoteItemResponse404> Invalid access
     * @throws FetchError<409, types.UpdateStickyNoteItemResponse409> Conflict
     * @throws FetchError<429, types.UpdateStickyNoteItemResponse429> Too many requests
     */
    updateStickyNoteItem(body: types.UpdateStickyNoteItemBodyParam, metadata: types.UpdateStickyNoteItemMetadataParam): Promise<FetchResponse<200, types.UpdateStickyNoteItemResponse200>>;
    /**
     * Deletes a sticky note item from the board.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 3</a><br/>
     *
     * @summary Delete sticky note item
     * @throws FetchError<400, types.DeleteStickyNoteItemResponse400> Malformed request
     * @throws FetchError<404, types.DeleteStickyNoteItemResponse404> Invalid access
     * @throws FetchError<429, types.DeleteStickyNoteItemResponse429> Too many requests
     */
    deleteStickyNoteItem(metadata: types.DeleteStickyNoteItemMetadataParam): Promise<FetchResponse<204, types.DeleteStickyNoteItemResponse204>>;
    /**
     * Adds a text item to a board.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Create text item
     * @throws FetchError<400, types.CreateTextItemResponse400> Malformed request
     * @throws FetchError<404, types.CreateTextItemResponse404> Invalid access
     * @throws FetchError<429, types.CreateTextItemResponse429> Too many requests
     */
    createTextItem(body: types.CreateTextItemBodyParam, metadata: types.CreateTextItemMetadataParam): Promise<FetchResponse<201, types.CreateTextItemResponse201>>;
    /**
     * Retrieves information for a specific text item on a board.<br/><h3>Required scope</h3>
     * <a target="blank" href="/reference/scopes">boards:read</a> <br/><h3>Rate limiting</h3>
     * <a target="blank" href="/reference/rate-limiting#rate-limit-tiers">Level 1</a><br/>
     *
     * @summary Get text item
     * @throws FetchError<400, types.GetTextItemResponse400> Malformed request
     * @throws FetchError<404, types.GetTextItemResponse404> Invalid access
     * @throws FetchError<429, types.GetTextItemResponse429> Too many requests
     */
    getTextItem(metadata: types.GetTextItemMetadataParam): Promise<FetchResponse<200, types.GetTextItemResponse200>>;
    /**
     * Updates a text item on a board based on the data and style properties provided in the
     * request body.<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 2</a><br/>
     *
     * @summary Update text item
     * @throws FetchError<400, types.UpdateTextItemResponse400> Malformed request
     * @throws FetchError<404, types.UpdateTextItemResponse404> Invalid access
     * @throws FetchError<409, types.UpdateTextItemResponse409> Conflict
     * @throws FetchError<429, types.UpdateTextItemResponse429> Too many requests
     */
    updateTextItem(body: types.UpdateTextItemBodyParam, metadata: types.UpdateTextItemMetadataParam): Promise<FetchResponse<200, types.UpdateTextItemResponse200>>;
    /**
     * Deletes a text item from the board<br/><h3>Required scope</h3> <a target="blank"
     * href="/reference/scopes">boards:write</a> <br/><h3>Rate limiting</h3> <a target="blank"
     * href="/reference/rate-limiting#rate-limit-tiers">Level 3</a><br/>
     *
     * @summary Delete text item
     * @throws FetchError<400, types.DeleteTextItemResponse400> Malformed request
     * @throws FetchError<404, types.DeleteTextItemResponse404> Invalid access
     * @throws FetchError<429, types.DeleteTextItemResponse429> Too many requests
     */
    deleteTextItem(metadata: types.DeleteTextItemMetadataParam): Promise<FetchResponse<204, types.DeleteTextItemResponse204>>;
}
declare const createSDK: SDK;
export default createSDK;
