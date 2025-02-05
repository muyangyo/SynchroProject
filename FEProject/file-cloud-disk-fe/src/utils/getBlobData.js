import {optionalRequest, RequestMethods} from "@/utils/RequestTool.js";

export default async function getBlobData(relativeURL, data, errorCallback) {
    return await optionalRequest({
        method: RequestMethods.POST,
        url: "/file" + relativeURL,
        data: JSON.stringify(data),
        responseType: 'blob',
        errorCallback: errorCallback,
    });
}