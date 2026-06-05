import "./agreement.less"
//外部引入
import '../../../assets/css/markdown.css';
// 底部说明
import "../../common/footer/footer.js";
// 回到顶部
import "../../common/back-top/back-top"
// markdown解析工具包
import {marked} from "marked";

const markdownBody = document.getElementById("markdownBody");
if (markdownBody) {
    const content = markdownBody.getAttribute("data-content")
    if (content) {
        document.getElementById("markdownBody").innerHTML = marked.parse(content)
        markdownBody.removeAttribute("data-content")
    }
}