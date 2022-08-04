import Main from "layouts/main";
import useTags from "hooks/useTags";
import { useState, useEffect } from "react";
import Select from "react-select";
import useUser from "hooks/useUser";
import { toast } from "react-toastify";
import { userService } from "service/user.service";
import useUserTags from "hooks/useUserTags";

export default function Profile() {
  const user = useUser();
  const preferredTags = useUserTags();
  const token = user?.token;
  const [details, setDetails] = useState([]);
  const [firstname, setfirstname] = useState("");
  const [lastname, setlastname] = useState("");
  const [email, setemail] = useState("");
  const handleFirstname = (e) => setfirstname(e.target.value);
  const handleLastname = (e) => setlastname(e.target.value);
  const handleEmail = (e) => setemail(e.target.value);
  const handleSubmit = () => {
    return userService
      .updateDetails(
        {
          userName: details.data.userName,
          firstName: firstname.length > 0 ? firstname : details.data.firstName,
          lastName: lastname.length > 0 ? lastname : details.data.lastName,
          emailID: email.length > 0 ? email : details.data.emailID,
        },
        token
      )
      .then((data) => {
        if (data["statusCode"] !== 200) {
          toast.error(`Error: ${JSON.stringify(data["message"])}`);
        } else {
          toast.success("Details Updated Successfully");
        }
      })
      .catch((e) => {
        console.log(e);
        toast.error("Error while updating details");
      });
  };
  useEffect(() => {
    async function get() {
      const token = user?.token ?? null;
      setDetails(await userService.getDetails(token));
    }
    get();
  }, [user?.token, user]);
  useEffect(() => {
    setSelectedTags(preferredTags);
  }, [preferredTags]);
  const [selectedTags, setSelectedTags] = useState([]);
  const handleTags = (tags) => setSelectedTags(tags);
  const tags = useTags();
  const submitTags = () => {
    const t = selectedTags.map((t) => {
      return {
        id: Number(t.value),
        tagName: t.label,
      };
    });
    return userService
      .saveTags(t, token)
      .then((data) => {
        if (data["statusCode"] !== 200) {
          toast.error(`Error: ${JSON.stringify(data["message"])}`);
        } else {
          toast.success("Preferences Saved Successfully");
        }
      })
      .catch((e) => {
        console.log(e);
        toast.error("Error while updating details");
      });
  };
  return (
    <Main title="Profile">
      <div className="grid grid-cols-1 gap-3 w-full lg:w-96">
        <div className="flex flex-row">
          <label htmlFor="userName" className="mr-2 font-semibold">
            User Name
          </label>
          <input
            className="bg-slate-50 outline-none p-2 rounded-lg border-2 border-black"
            type="text"
            name="userName"
            id="userName"
            placeholder={details.data?.userName}
            disabled
          />
        </div>
        <div className="flex flex-row">
          <label htmlFor="firstName" className="mr-2 font-semibold">
            First Name
          </label>
          <input
            className="bg-slate-50 outline-none p-2 rounded-lg border-2 border-black"
            type="text"
            name="firstName"
            id="firstName"
            placeholder={details.data?.firstName}
            onChange={handleFirstname}
          />
        </div>
        <div className="flex flex-row">
          <label htmlFor="lastName" className="mr-2 font-semibold">
            Last Name
          </label>
          <input
            className="bg-slate-50 outline-none p-2 rounded-lg border-2 border-black"
            type="text"
            name="lastName"
            id="lastName"
            placeholder={details.data?.lastName}
            onChange={handleLastname}
          />
        </div>
        <div className="flex flex-row">
          <label htmlFor="emailID" className="lg:mr-4 mr-4 font-semibold">
            Email ID &nbsp;
          </label>
          <input
            className="bg-slate-50 outline-none p-2 rounded-lg border-2 border-black"
            type="email"
            name="emailID"
            id="emailID"
            placeholder={details.data?.emailID}
            onChange={handleEmail}
          />
        </div>
        <div className="text-center">
          <button
            className="border-black border-2 rounded-md font-semibold hover:bg-black hover:text-white mt-4 mb-5 p-2"
            type="submit"
            onClick={handleSubmit}
          >
            Update
          </button>
        </div>
        <div className="flex flex-row">
          <label htmlFor="lastName" className="mr-1 font-semibold">
            Preferences
          </label>
          <div className="w-48 mr-3">
            <Select
              options={tags}
              value={selectedTags}
              isMulti
              placeholder="Select Tags"
              instanceId={"tags"}
              onChange={handleTags}
              hideSelectedOptions
            />
          </div>
          <button
            className="text-base border-black border-2 rounded-md font-semibold hover:bg-black hover:text-white w-16 h-9"
            type="submit"
            onClick={submitTags}
          >
            Save
          </button>
        </div>
      </div>
    </Main>
  );
}
