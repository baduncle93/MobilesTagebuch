$(document).ready(function() {
  const server = 'localhost:8080';

  let canvas = $('#myCanvas');
  let context = canvas[0].getContext('2d');
  let imageData;
  let imageObj = new Image();
  let texInput = $('#inputText');
  let textSize = $('#textSize');
  let drawingColor = $('#drawingColor');
  let description = $('#descriptionText');

  let editMode = 999; //0=Draw 1=Text 2=Highlighting 3=Enumeration 4=Area Highlighting 5=Arrow
  let startX = 0;
  let startY = 0;
  let mouseX = 0;
  let mouseY = 0;
  let mouseIsUp = 1;
  let eNumb = 1;
  let elv = 0;

  let undoCanv = [];

  let currentTwitterPostId = '';
  let currentTwitterPostUser = '';

  imageObj.onload = function() {
    refreshImagePreview();
    context.drawImage(imageObj, 5, 5, canvas[0].width - 10, canvas[0].height - 10);
    imageData = context.getImageData(0, 0, canvas[0].width - 10, canvas[0].height - 10);
  };

  //source of the dummy img first displayed on the canvas
  imageObj.src = 'images/dummyImage.png';
  imageString = canvas[0].toDataURL();

  //used for displaying all the tooltips
  $(function() {
    $('[data-toggle="tooltip"]').tooltip();
  });

  $('#textSize').on('change', function(e) {
    $('#inputText').css('font-size', $('#textSize').val() + 'px');
    $('#textSize').css('font-size', $('#textSize').val() + 'px');
  });

  //the Twitter-login functionality was planned here
  /* 
  $('#twitterLogin').on('click', function(e) {
    let body = {
      imagePath: {
        value: imgPath.data['fileId']
      },
      tweet: {
        value: description.val()
      }
    };

    const postResponse = fetch('http://localhost', {
      method: 'post',
      body: JSON.stringify(body),
      headers: {
        'Content-Type': 'application/json'
      }
    });
  });
*/

  //Onclick function for invoking onclick on File Input
  $('#uploadBtn').on('click', function(e) {
    document.getElementById('fileid').click();
  });

  //Onchange function for image upload to server
  const input = document.querySelector('input[type="file"]');
  input.addEventListener(
    'change',
    function(e) {
      const reader = new FileReader();
      reader.onload = function() {
        console.log(saveCanvasToServer(reader.result));
      };
      reader.readAsDataURL(input.files[0]);
    },
    false
  );

  //Onclick function for receiving images from twitter posts
  $('#postImgBtn').on('click', function(e) {
    let searchText = prompt('Enter a keyword for the post you want to receive:', 'Search-Term');
    if (searchText == null || searchText == '') {
      console.log('action cancelled!');
    } else {
      let response = getPostsFromTwitter(searchText);
    }
  });

  //Onclick function for changing editing method via the "radio-buttons"
  $('input:radio[name=meth]').click(function() {
    editMode = parseInt($('input:radio[name=meth]:checked').val(), 10);
    console.log('changing method to: ' + editMode);
    texInput.css('visibility', 'hidden');
    textSize.css('visibility', 'hidden');
    drawingColor.css('visibility', 'hidden');
    if (editMode === 1) {
      texInput.css('visibility', 'visible');
      textSize.css('visibility', 'visible');
    }
    if (editMode === 0 || editMode === 1 || editMode === 3 || editMode === 5) {
      drawingColor.css('visibility', 'visible');
    }
  });

  //Onclick function for undo operation
  $('#undoBtn').on('click', function(e) {
    if (undoCanv.length > 0) {
      context.putImageData(undoCanv.pop(), 0, 0);
    } else {
      context.putImageData(imageData, 0, 0);
    }
  });

  //Onclick function for saving the edited image locally
  $('#saveBtn').on('click', function(e) {
    saveCanvasImage();
    // createImageForPreview(src);
  });

  //Onclick function for posting to Twitter
  $('#postBtn').on('click', function(e) {
    postCanvasImage();
  });

  //Onclick function for opening a preview image in the canvas
  $(document).on('click', '.previewImage', function() {
    if (confirm('Do you really want to discard your changes and use another image?')) {
      context.canvas.width = context.canvas.width;
      imageObj.src = this.src;
      if (this.alt && this.name) {
        currentTwitterPostId = this.alt;
        currentTwitterPostUser = '@' + this.name;
      } else {
        currentTwitterPostId = '';
        currentTwitterPostUser = '';
      }
      console.log('User: ' + currentTwitterPostUser);
      console.log('Id: ' + currentTwitterPostId);
      //clearing undoarray
      imageData = context.getImageData(0, 0, canvas[0].width - 10, canvas[0].height - 10);
      undoCanv = [];
    }
  });

  $(document).mousedown(function(e) {
    mouseIsUp = 0;
  });

  $(document).mouseup(function(e) {
    mouseIsUp = 1;
    context.beginPath();
  });

  canvas.mouseup(function(e) {
    if (editMode === 5 && elv) {
      printArrowOnImg();
    }
    if (editMode === 4 && elv && startX != mouseX) {
      highlightImgArea();
    }
  });

  canvas.mousedown(function(e) {
    switch (editMode) {
      case 1:
        printTextOnImg(e);
        break;
      case 3:
        printNumberOnImg(e);
        break;
      case 4:
      case 5:
        saveState();
        startX = e.clientX - canvas[0].getBoundingClientRect().x;
        startY = e.clientY - canvas[0].getBoundingClientRect().y;
        elv = 1;
        break;
    }
  });

  canvas.mousemove(function(e) {
    mouseX = e.clientX - canvas[0].getBoundingClientRect().x;
    mouseY = e.clientY - canvas[0].getBoundingClientRect().y;
    if (!mouseIsUp) {
      if (editMode === 0 || editMode === 2) {
        drawLineOnImg(e);
        context.fillStyle = 'black';
        stroke = 2;
      }
    }
  });

  //converting touch to mouseenvent
  canvas[0].addEventListener(
    'touchstart',
    function(e) {
      let touch = e.touches[0];
      let mouseEvent = new MouseEvent('mousedown', {
        clientX: touch.clientX,
        clientY: touch.clientY
      });
      mouseIsUp = 0;
      canvas[0].dispatchEvent(mouseEvent);
    },
    false
  );

  canvas[0].addEventListener(
    'touchend',
    function(e) {
      e.preventDefault();
      let mouseEvent = new MouseEvent('mouseup', {});
      mouseIsUp = 1;
      canvas[0].dispatchEvent(mouseEvent);
    },
    false
  );

  //!NEW NEEDS TESTING
  document.addEventListener('touchend', function(e) {
    e.preventDefault();
    let mouseEvent = new MouseEvent('mouseup', {});
    mouseIsUp = 1;
    document.dispatchEvent(mouseEvent);
  });

  canvas[0].addEventListener(
    'touchmove',
    function(e) {
      e.preventDefault();
      let touch = e.touches[0];
      let mouseEvent = new MouseEvent('mousemove', {
        clientX: touch.clientX,
        clientY: touch.clientY
      });
      canvas[0].dispatchEvent(mouseEvent);
    },
    false
  );

  //*Drawing functions
  function drawLineOnImg(e) {
    context.lineWidth = 6;
    context.strokeStyle = drawingColor.val();
    context.lineCap = 'round';
    if (editMode === 2) {
      context.lineWidth = 18;
      context.strokeStyle = 'rgba(255, 255, 0, 0.06)';
      stroke = 10;
    }
    context.lineTo(
      e.clientX - canvas[0].getBoundingClientRect().x,
      e.clientY - canvas[0].getBoundingClientRect().y
    );
    context.stroke();
    context.beginPath();
    context.moveTo(
      e.clientX - canvas[0].getBoundingClientRect().x,
      e.clientY - canvas[0].getBoundingClientRect().y
    );
  }

  function printTextOnImg(e) {
    if (texInput.val()) {
      saveState();
      context.font = textSize.val() + 'px Georgia';
      context.fillStyle = drawingColor.val();
      context.fillText(
        texInput.val(),
        e.clientX - canvas[0].getBoundingClientRect().x,
        e.clientY - canvas[0].getBoundingClientRect().y
      );
    } else {
      alert('Enter a text before drawing it on the image!');
    }
  }

  function printNumberOnImg(e) {
    if (eNumb === 11) {
      if (confirm("Maximum number of Enumerations reached! Want to reset to '1'?")) {
        eNumb = 1;
      }
      return;
    }
    console.log('numbering...');
    saveState();
    context.font = 30 + 'px Georgia';
    context.fillStyle = drawingColor.val();
    context.fillText(
      eNumb + '.',
      e.clientX - canvas[0].getBoundingClientRect().x,
      e.clientY - canvas[0].getBoundingClientRect().y
    );
    eNumb++;
  }

  function printArrowOnImg() {
    context.fillStyle = drawingColor.val();
    context.strokeStyle = drawingColor.val();
    context.beginPath();
    context.lineWidth = 2;
    context.moveTo(startX, startY);
    context.lineTo(mouseX, mouseY);
    let dist = Math.sqrt(
      (startX - mouseX) * (startX - mouseX) + (startY - mouseY) * (startY - mouseY)
    );
    let angle = Math.acos((startY - mouseY) / dist);
    if (startX < mouseX) angle = 2 * Math.PI - angle;
    let size = 20;
    context.closePath();
    context.stroke();
    context.beginPath();
    context.translate(mouseX, mouseY);
    context.rotate(-angle);
    context.lineTo(0 + size / 3.5, 0 + size);
    context.lineTo(0, 0);
    context.lineTo(0 - size / 3.5, 0 + size);
    context.lineTo(0, 0 + size);
    context.closePath();
    context.fill();
    context.stroke();
    context.rotate(+angle);
    context.translate(-mouseX, -mouseY);
    elv = 0;
  }

  function highlightImgArea(e) {
    let topCornerX = Math.min(mouseX, startX);
    let topCornerY = Math.min(mouseY, startY);
    /* this would draw a rectangle around the highlighted area
      context.beginPath();
      context.rect(startX, startY, mouseX - startX, mouseY - startY);
      context.closePath();
      context.stroke();
      */
    elv = 0;
    imageHighlight = context.getImageData(startX, startY, mouseX - startX, mouseY - startY);
    let imageDataSave = context.getImageData(5, 5, 990, 682);
    for (let i = 0; i < imageDataSave.data.length; i += 4) {
      imageDataSave.data[i + 3] = 100;
    }
    for (let i = 0; i < imageHighlight.data.length; i += 4) {
      imageHighlight.data[i + 3] = 255;
    }
    context.putImageData(imageDataSave, 5, 5);
    context.putImageData(imageHighlight, topCornerX, topCornerY);
  }

  //saves the current canvas into an array (used for undo)
  function saveState() {
    undoCanv.push(context.getImageData(0, 0, 1000, 692));
  }
  //function for downloading the canvas image locally
  function saveCanvasImage() {
    let dataURI = canvas[0].toDataURL('image/png');
    const a = document.createElement('a');
    document.body.appendChild(a);
    a.href = canvas[0].toDataURL();
    a.download = 'canvas-image' + Date.now() + '.png';
    a.click();
    document.body.removeChild(a);
  }

  //*Network functions
  //function for posting the canvas image and the text to twitter
  async function postCanvasImage() {
    if (description.val() === '') {
      alert('You must enter a descriptive text before posting to Twitter!');
    } else {
      if (
        currentTwitterPostUser === '' ||
        confirm('By confirming you will reply to the post of ' + currentTwitterPostUser)
      ) {
        document.body.style.cursor = 'wait';
        let imgPath = await saveCanvasToServer();

        let originalImg = imageObj.src.substring(imageObj.src.search(';') + 8);
        let editedImg = canvas[0].toDataURL().substring(canvas[0].toDataURL().search(';') + 8);
        let body = {
          original: {
            value: originalImg
          },
          edited: {
            value: editedImg
          },
          tweet: {
            value: description.val() + ' ' + currentTwitterPostUser
          },
          postId: {
            value: currentTwitterPostId
          }
        };

        const postResponse = await fetch(
          'http://' +
            server +
            '/micro-service-controller-rest/rest/msc/callMicroserviceForced?microserviceId=6ea700a4-92f2-427d-9e87-0528f2b3db90&operationId=default',
          {
            method: 'post',
            body: JSON.stringify(body),
            headers: {
              'Content-Type': 'application/json'
            }
          }
        );
        await console.log(postResponse.json());
        if (postResponse.status === 200) {
          document.body.style.cursor = 'default';
          alert('Sucessfully posted to Twitter!');
        }
      }
    }
  }

  //function for saving the posted image to the server
  async function saveCanvasToServer(imageStr) {
    let dataURI;
    if (imageStr) {
      dataURI = imageStr;
    } else {
      dataURI = canvas[0].toDataURL('image/png');
    }
    let str = dataURI.substring(dataURI.search(';') + 8);
    let imgtype = dataURI.substring(11, dataURI.search(';'));
    let body = {
      fileName: {
        value: 'canvas-image' + Date.now() + '.' + imgtype
      },
      content: {
        value: str
      }
    };
    const saveResponse = await fetch(
      'http://' +
        server +
        '/micro-service-controller-rest/rest/msc/callMicroserviceForced?microserviceId=354c9456-0b24-4795-b3ee-fdcc9f6d07a0&operationId=default',
      {
        method: 'post',
        body: JSON.stringify(body),
        headers: {
          'Content-Type': 'application/json'
        }
      }
    );
    refreshImagePreview();
    return saveResponse.json();
  }

  //function for adding images to the preview box
  async function refreshImagePreview() {
    document.body.style.cursor = 'wait';
    let body = {};
    const imgArr = await fetch(
      'http://' +
        server +
        '/micro-service-controller-rest/rest/msc/callMicroserviceForced?microserviceId=583c2e80-a7ac-45c9-9f89-2f53248f6f1a&operationId=default',
      {
        method: 'post',
        body: JSON.stringify(body),
        headers: {
          'Content-Type': 'application/json'
        }
      }
    )
      .then(function(response) {
        return response.json();
      })
      .then(function(data) {
        const parentElement = document.getElementById('imageList');
        while (parentElement.firstChild) {
          parentElement.removeChild(parentElement.firstChild);
        }
        for (let i = 0; i < data.data.imgPaths.length; i++) {
          let newImageLi = document.createElement('li');
          newImageLi.setAttribute('id', 'Img' + i);
          document.getElementById('imageList').appendChild(newImageLi);
          let newImage = document.createElement('img');
          newImage.setAttribute('class', 'previewImage');
          newImage.src = 'data:image/png;base64,' + data.data.imgPaths[i].imgPath;
          document.getElementById('Img' + i).appendChild(newImage);
        }
      });
    document.body.style.cursor = 'default';
  }

  //receives images from Twitter posts in response for the searchText
  async function getPostsFromTwitter(searchText) {
    let body = {
      query: {
        value: searchText
      }
    };
    const searchResponse = await fetch(
      'http://' +
        server +
        '/micro-service-controller-rest/rest/msc/callMicroserviceForced?microserviceId=38e74c1d-dfd6-4529-80b8-e789e8fe2410&operationId=default',
      {
        method: 'post',
        body: JSON.stringify(body),
        headers: {
          'Content-Type': 'application/json'
        }
      }
    )
      .then(function(response) {
        return response.json();
      })
      .then(function(data) {
        console.log(data);
        let postArray = data.data.imgStrings;
        if (postArray.length) {
          let list = document.getElementById('twitterImageList');
          while (list.firstChild) {
            list.removeChild(list.firstChild);
          }

          for (let i = 0; i < postArray.length; i++) {
            let newImageLi = document.createElement('li');
            newImageLi.setAttribute('id', 'TwitterImg' + i);
            document.getElementById('twitterImageList').appendChild(newImageLi);
            let newImage = document.createElement('img');
            newImage.setAttribute('class', 'previewImage');
            newImage.setAttribute('name', '' + postArray[i].userName);
            newImage.setAttribute('alt', '' + postArray[i].postId);
            newImage.setAttribute('data-toggle', 'tooltip');
            newImage.setAttribute('title', 'username: ' + postArray[i].userName);
            newImage.src = 'data:image/png;base64,' + postArray[i].imgString;
            document.getElementById('TwitterImg' + i).appendChild(newImage);
          }
        } else {
          alert('No such posts found on Twitter. Try again!');
        }
      });
  }
});
