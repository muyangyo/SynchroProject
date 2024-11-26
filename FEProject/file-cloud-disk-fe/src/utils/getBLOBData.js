import {optionalRequest, RequestMethods} from "@/utils/RequestTool.js";

export default async function getBlobData(relativeURL) {
    return await optionalRequest({
        method: RequestMethods.POST,
        url: "/file" + relativeURL,
        responseType: 'blob'
    });
}