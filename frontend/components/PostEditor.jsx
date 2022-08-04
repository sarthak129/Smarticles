import React from "react";
import { useEditor, EditorContent } from "@tiptap/react";
import StarterKit from "@tiptap/starter-kit";
const EditorButton = ({ children, className }) => {
  return (
    <span
      className={`
      ${className}
      rounded-md ring-black border-black border-2 px-2 mr-1
    `}
    >
      {children}
    </span>
  );
};
const TopMenuBar = ({ editor }) => {
  if (!editor) {
    return null;
  }

  return (
    <div className="mt-10">
      <EditorButton
        className={
          editor.isActive("bold")
            ? "bg-black text-white"
            : "bg-white text-black"
        }
      >
        <button onClick={() => editor.chain().focus().toggleBold().run()}>
          bold
        </button>
      </EditorButton>
      <EditorButton
        className={
          editor.isActive("italic")
            ? "bg-black text-white"
            : "bg-white text-black"
        }
      >
        <button onClick={() => editor.chain().focus().toggleItalic().run()}>
          italic
        </button>
      </EditorButton>
      <EditorButton
        className={
          editor.isActive("strike")
            ? "bg-black text-white"
            : "bg-white text-black"
        }
      >
        <button
          onClick={() => editor.chain().focus().toggleStrike().run()}
          className={editor.isActive("strike") ? "is-active" : ""}
        >
          strike
        </button>
      </EditorButton>
      <EditorButton
        className={
          editor.isActive("code")
            ? "bg-black text-white"
            : "bg-white text-black"
        }
      >
        <button
          onClick={() => editor.chain().focus().toggleCode().run()}
          className={editor.isActive("code") ? "is-active" : ""}
        >
          code
        </button>
      </EditorButton>
      <EditorButton>
        <button onClick={() => editor.chain().focus().unsetAllMarks().run()}>
          clear formatting
        </button>
      </EditorButton>
      <EditorButton
        className={
          editor.isActive("heading", { level: 1 })
            ? "bg-black text-white"
            : "bg-white text-black"
        }
      >
        <button
          onClick={() =>
            editor.chain().focus().toggleHeading({ level: 1 }).run()
          }
        >
          h1
        </button>
      </EditorButton>
      <EditorButton
        className={
          editor.isActive("heading", { level: 2 })
            ? "bg-black text-white"
            : "bg-white text-black"
        }
      >
        <button
          onClick={() =>
            editor.chain().focus().toggleHeading({ level: 2 }).run()
          }
          className={
            editor.isActive("heading", { level: 2 }) ? "is-active" : ""
          }
        >
          h2
        </button>
      </EditorButton>
      <EditorButton
        className={
          editor.isActive("heading", { level: 3 })
            ? "bg-black text-white"
            : "bg-white text-black"
        }
      >
        <button
          onClick={() =>
            editor.chain().focus().toggleHeading({ level: 3 }).run()
          }
          className={
            editor.isActive("heading", { level: 3 }) ? "is-active" : ""
          }
        >
          h3
        </button>
      </EditorButton>
      <EditorButton
        className={
          editor.isActive("bulletList")
            ? "bg-black text-white"
            : "bg-white text-black"
        }
      >
        <button
          onClick={() => editor.chain().focus().toggleBulletList().run()}
          className={editor.isActive("bulletList") ? "is-active" : ""}
        >
          bullet list
        </button>
      </EditorButton>
      <EditorButton
        className={
          editor.isActive("orderedList")
            ? "bg-black text-white"
            : "bg-white text-black"
        }
      >
        <button
          onClick={() => editor.chain().focus().toggleOrderedList().run()}
          className={editor.isActive("orderedList") ? "is-active" : ""}
        >
          ordered list
        </button>
      </EditorButton>
      <EditorButton
        className={
          editor.isActive("codeBlock")
            ? "bg-black text-white"
            : "bg-white text-black"
        }
      >
        <button
          onClick={() => editor.chain().focus().toggleCodeBlock().run()}
          className={editor.isActive("codeBlock") ? "is-active" : ""}
        >
          code block
        </button>
      </EditorButton>
      <EditorButton
        className={
          editor.isActive("blockquote")
            ? "bg-black text-white"
            : "bg-white text-black"
        }
      >
        <button
          onClick={() => editor.chain().focus().toggleBlockquote().run()}
          className={editor.isActive("blockquote") ? "is-active" : ""}
        >
          blockquote
        </button>
      </EditorButton>
      <EditorButton className={"bg-white text-black"}>
        <button
          onClick={() => editor.chain().focus().setHorizontalRule().run()}
        >
          horizontal rule
        </button>
      </EditorButton>
    </div>
  );
};
const BottomBar = ({ editor }) => {
  if (!editor) {
    return null;
  }
  return (
    <div className="text-center mt-10">
      <EditorButton className={"bg-white text-black"}>
        <button onClick={() => editor.chain().focus().undo().run()}>
          undo
        </button>
      </EditorButton>
      <EditorButton className={"bg-white text-black"}>
        <button onClick={() => editor.chain().focus().redo().run()}>
          redo
        </button>
      </EditorButton>
    </div>
  );
};

export default function Editor({ handleChange }) {
  const editor = useEditor({
    extensions: [StarterKit],
    content: `
    <p>
      Start writing your new post...
    </p>
    <p>
      Use the buttons above for formatting
    </p>
      `,
    editorProps: {
      attributes: {
        class: `prose focus:outline-none prose-stone leading-0.5 border-2 border-black rounded-md px-3 mt-10 my-3 h-96 max-h-96 overflow-scroll mx-auto`,
      },
    },
    onUpdate: handleChange,
  });

  return (
    <div className="mt-5 text-center">
      <TopMenuBar editor={editor} />
      <EditorContent editor={editor} />
      <BottomBar editor={editor} />
    </div>
  );
}
