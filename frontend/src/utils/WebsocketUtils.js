import {Stomp} from "@stomp/stompjs";

export function establishSockJs(
  url,
  destination,
  onConnectionCallback,
  onMessageCallback,
  onErrorCallback) {
  // Create SockJS connection
  const SockJs = window.SockJS;
  const socket = new SockJs(url);

  // Wrap SockJS with STOMP
  const stompClient = Stomp.over(socket);

  // Connect to STOMP server
  stompClient.connect({}, function (frame) {
    onConnectionCallback(frame)
    // Subscribe to a destination
    stompClient.subscribe(destination, function (message) {
      onMessageCallback(message)
    });
    // Optional: Send a message to a destination
    // stompClient.send("/app/ping", {}, JSON.stringify({'type': 'ping'}));
  }, function (error) {
    onErrorCallback(error)
  });

  return stompClient
}
