import './version-doc.less';
// 文章解析样式
import '../../../assets/plugins/markdown/markdown.css';
// 回到顶部
import '../../common/back-top/back-top';
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
