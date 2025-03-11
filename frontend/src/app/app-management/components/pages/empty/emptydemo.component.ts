import { Component } from '@angular/core';

@Component({
    templateUrl: './emptydemo.component.html'
})
export class EmptyDemoComponent { 
    // public connect(p: any) {
    //     const ws = new SockJS(CHAT_URL);
    //     ws.onmessage = data => {
    //         const result = JSON.parse(data.data).data;
    //         if (result.serverType === 'HANDLE') {
    //             return;
    //         }
    //         if (p.items.size === 0) {
    //             p.add(result, true, null);
    //             return;
    //         }
    //         const node = p.items.get(result.appName);
    //         if (node === undefined || node === null) {
    //             p.add(result, true, null);
    //             return;
    //         }
    //         p.add(result, false, node);
    //     };

    //     ws.onerror = function(e) {
    //         p.reconnect();
    //     };

    //     ws.onclose = function(e) {
    //         p.reconnect();
    //     };
    // }
}
