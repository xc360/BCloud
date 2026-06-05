export default class XcDialog {
    constructor(ejectId, confirmId, closeId, data) {
        this.ejectDom = document.getElementById(ejectId);
        this.confirmDom = document.getElementById(confirmId);
        this.closeDom = document.getElementById(closeId);
        this.data = data;
    }

    open(type) {
        this.ejectDom.style.display = 'block';
        this.type = type;
    }


    close(type) {
        this.ejectDom.style.display = 'none';
        this.type = type;
    }

    /**
     * 关闭按钮回调
     */
    closeCallback(fun) {
        const that = this;
        // 确认弹窗
        this.closeDom.addEventListener('click', function(e) {
            if (fun) {
                if (fun(that, e)) {
                    that.close(this.type);
                }
            } else {
                that.close(this.type);
            }
        }, false);
    }

    /**
     * 确认按钮回调
     */
    confirmCallback(fun) {
        const that = this;
        // 确认弹窗
        this.confirmDom.addEventListener('click', function(e) {
            if (fun) {
                if (fun(that, e)) {
                    that.close(this.type);
                }
            } else {
                that.close(this.type);
            }
        }, false);
    }
}
